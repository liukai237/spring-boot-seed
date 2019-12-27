package com.yodinfo.seed.auth;

import com.yodinfo.seed.domain.User;
import com.yodinfo.seed.service.UserService;
import com.yodinfo.seed.util.PasswordHash;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class DbRealm extends BasicRealm {

    public DbRealm(UserService userService) {
        super(userService);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String uid = usernamePasswordToken.getUsername();
        char[] password = usernamePasswordToken.getPassword();
        User user = userService.findById(Long.parseLong(uid));

        boolean result;
        try {
            result = PasswordHash.validatePassword(password, user.getPasswdHash());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AuthenticationException("invalid password!");
        }

        Map<String, String> details = userService.findUserDetails(uid);
        return result ? new SimpleAuthenticationInfo(details, password, "dbRealm") : null;
    }
}
