package com.iakuil.bf.shiro;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iakuil.bf.common.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Session服务
 *
 * @author Kai
 */
@Slf4j
@Service
public class SessionService {

    private static final int MAX_SESSION_SIZE = 3;

    @Resource
    private RedisSessionDAO sessionDAO;

    public Map<Long, List<Session>> getOnlineSessions() {
        Map<Long, List<Session>> results = Maps.newHashMap();
        for (Session session : sessionDAO.getActiveSessions()) {
            Long userId = getUserIdFromSession(session);
            List<Session> tmpList = results.getOrDefault(userId, Lists.newArrayList());
            tmpList.add(session);
            results.put(userId, tmpList);
        }
        return results;
    }

    public void kickOutFor(Session session) {
        Long userId = getUserIdFromSession(session);
        this.kickOutFor(userId);
    }

    public void kickOutFor(Long userId) {
        List<Session> sessions = getSessionsByUserId(userId);
        sessions.stream()
                .sorted(Comparator.comparing(Session::getStartTimestamp))
                .skip(MAX_SESSION_SIZE)
                .forEach(item -> sessionDAO.delete(item));
    }

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
        UserDetails userDetails = (UserDetails) spc.getPrimaryPrincipal();
        return userDetails == null ? -1L : userDetails.getId();
    }
}
