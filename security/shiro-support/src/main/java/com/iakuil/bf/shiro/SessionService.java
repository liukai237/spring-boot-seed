package com.iakuil.bf.shiro;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iakuil.bf.common.security.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Session服务
 *
 * <p>{@link SessionDAO}之上再封装一层，实现一些Session的常用操作。
 *
 * @author Kai
 */
@Slf4j
public class SessionService {

    private static final int MAX_SESSION_SIZE = 3;

    private final SessionDAO sessionDAO;

    public SessionService(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    /**
     * 查询当前在线的Session列表
     *
     * <p>不包括RememberMe的Session。
     */
    public Map<Long, List<Session>> getOnlineSessions() {
        Map<Long, List<Session>> results = Maps.newHashMap();
        for (Session session : sessionDAO.getActiveSessions()) {
            Long userId = getUserIdFromSession(session);
            if (userId == null) {
                continue;
            }
            List<Session> tmpList = results.getOrDefault(userId, Lists.newArrayList());
            tmpList.add(session);
            results.put(userId, tmpList.stream().sorted(Comparator.comparing(Session::getStartTimestamp)).collect(Collectors.toList()));
        }
        return results;
    }

    /**
     * 管理员踢人
     *
     * <p>一般配合{@link this#getOnlineSessions()}使用。
     */
    public void kickOut(Session session) {
        sessionDAO.delete(session);
    }

    /**
     * 用户登陆后踢人
     *
     * <p>同一用户，超过三个Session时，踢掉最老的Session
     */
    @Async
    public void kickOutFor(Long userId) {
        if (userId == null) {
            return;
        }
        List<Session> sessions = getSessionsByUserId(userId);
        sessions.stream()
                .sorted(Comparator.comparing(Session::getStartTimestamp).reversed())
                .skip(MAX_SESSION_SIZE)
                .forEach(sessionDAO::delete);
    }

    /**
     * 认证信息变更后刷新相关Session
     */
    public void refresh(UserDetails userDetails) {
        List<Session> sessions = getSessionsByUserId(userDetails.getId());
        sessions.stream()
                .sorted(Comparator.comparing(Session::getStartTimestamp))
                .forEach(item -> {
                    SimplePrincipalCollection spc = (SimplePrincipalCollection) item.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                    UserDetails pp = (UserDetails) spc.getPrimaryPrincipal();
                    BeanUtils.copyProperties(userDetails, pp);
                    sessionDAO.update(item);
                });
    }

    private List<Session> getSessionsByUserId(Long userId) {
        List<Session> targets = Lists.newArrayList();
        for (Session session : sessionDAO.getActiveSessions()) {
            Long tmpId = getUserIdFromSession(session);
            if (Objects.equals(userId, tmpId)) {
                targets.add(session);
            }
        }

        return targets;
    }

    private Long getUserIdFromSession(Session session) {
        SimplePrincipalCollection spc = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (spc == null) {
            return null;
        }
        UserDetails userDetails = (UserDetails) spc.getPrimaryPrincipal();
        return userDetails == null ? null : userDetails.getId();
    }
}
