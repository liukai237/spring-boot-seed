package com.iakuil.bf.shiro;

import com.iakuil.bf.service.UserService;
import com.iakuil.bf.common.UserDetails;
import com.iakuil.toolkit.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
public class JdbcRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        SimpleAuthenticationInfo info;
        UserDetails user = userService.findUserDetails(username);
        if (user == null) {
            throw new UnknownAccountException("用户名未注册");
        }

        boolean checkResult = false;
        try {
            checkResult = PasswordHash.validatePassword(password, user.getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        if (!checkResult) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }

        info = new SimpleAuthenticationInfo(
                user,
                password,
                getName());
        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        UserDetails userDetails = (UserDetails) principals.getPrimaryPrincipal();
        if (userDetails != null) {
            info.setRoles(userDetails.getRoles());
            info.setStringPermissions(userDetails.getPermissions());
        }
        return info;
    }
}
