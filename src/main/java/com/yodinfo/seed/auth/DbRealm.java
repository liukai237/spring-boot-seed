package com.yodinfo.seed.auth;

import com.yodinfo.seed.domain.Role;
import com.yodinfo.seed.domain.User;
import com.yodinfo.seed.service.UserService;
import com.yodinfo.seed.util.PasswordHash;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.stream.Collectors;

public class DbRealm extends AuthorizingRealm {

    private UserService userService;

    public DbRealm(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        User user = userService.findByUserName2(username);

        boolean result;
        try {
            result = PasswordHash.validatePassword(usernamePasswordToken.getPassword(), user.getPasswdHash());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AuthenticationException("invalid password!");
        }

        return result ? new SimpleAuthenticationInfo(user.getUsername(), ((UsernamePasswordToken) token).getPassword(), "dbRealm") : null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println(principalCollection);
        Object principalObj = principalCollection.getPrimaryPrincipal();
        if (principalObj != null) {
            String uid = (String) principalObj;
            User user = userService.findByUserName2(uid);
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            Long userId = user.getUserId();
            List<Role> roles = userService.findRolesByUserId(userId);
            if (CollectionUtils.isNotEmpty(roles)) {
                info.addRoles(roles.stream().map(Role::getRoleName).collect(Collectors.toSet()));
            } else {
                return null;
            }

            for (Role role : roles) {
                info.addStringPermission(role.getRoleName());
            }
            return info;
        }
        return null;
    }
}
