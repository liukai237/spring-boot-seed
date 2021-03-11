package com.iakuil.bf.shiro;

import com.iakuil.bf.common.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录认证服务
 *
 * @author Kai
 */
@Slf4j
@Service
public class LoginService {

    /**
     * 通过用户名密码登录
     */
    @Transactional(readOnly = true)
    public UserDetails signIn(String username, String password, boolean rememberMe) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(username, password, rememberMe));

        // 登录成功返回用户基本信息
        return (UserDetails) subject.getPrincipal();
    }

    /**
     * 用户注销
     */
    public void logOut() {

    }
}
