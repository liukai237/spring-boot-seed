package com.iakuil.bf.shiro;

import com.iakuil.bf.common.exception.BusinessException;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.UserService;
import com.iakuil.bf.service.dto.BasicUserInfoDto;
import com.iakuil.toolkit.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户通过用户名密码登录
     */
    @Transactional(readOnly = true)
    public BasicUserInfoDto signIn(String username, String password, boolean rememberMe) {
        User user = userService.findUserByIdentity(username);
        if (user == null) {
            throw new BusinessException("用户不存在！");
        }

        // 执行登录操作
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(username, password, rememberMe));

        // 登录成功返回用户基本信息
        BasicUserInfoDto basicUserInfo = BeanUtils.copy(user, BasicUserInfoDto.class);
        basicUserInfo.setSessionId(subject.getSession().getId().toString());
        return basicUserInfo;
    }

    /**
     * 用户注销
     */
    public void logOut() {
        SecurityUtils.getSubject().logout();
    }
}