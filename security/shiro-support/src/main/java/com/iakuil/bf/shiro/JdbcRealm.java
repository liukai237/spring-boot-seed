package com.iakuil.bf.shiro;

import com.iakuil.bf.service.UserService;
import com.iakuil.toolkit.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

        SimpleAuthenticationInfo info = null;
        Map<String, String> user = userService.findUserDetails(username);
        if (user == null) {
            throw new UnknownAccountException("用户名未注册");
        }

        boolean checkResult = false;
        try {
            checkResult = PasswordHash.validatePassword(password, MapUtils.getString(user, "password"));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        if (!checkResult) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }

        info = new SimpleAuthenticationInfo(
                MapUtils.getString(user, "userId"),
                password,
                getName());
        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Set<String> roles = new HashSet<>();
        roles.add("admin"); //TODO 查询数据库后授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);
        return info;
    }
}
