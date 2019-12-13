package com.yodinfo.seed.auth;

import com.yodinfo.seed.domain.User;
import com.yodinfo.seed.service.UserService;
import com.yodinfo.seed.util.PasswordHash;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
        return null;
    }
}
