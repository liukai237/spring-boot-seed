package com.yodinfo.seed.auth;

import com.yodinfo.seed.service.UserService;
import com.yodinfo.seed.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

@Slf4j
public class JwtRealm extends BasicRealm {

    public JwtRealm(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String accessToken = (String) authenticationToken.getCredentials();
        String identityId = JwtUtils.getIdentity(accessToken);
        if (identityId == null) {
            throw new AuthenticationException("invalid token!");
        }
//        // 非最新
//        String accessTokenInCache = authTokenService.getAccessTokenFromCache(identityId);
//        if (accessTokenInCache == null || !accessTokenInCache.equals(accessToken)) {
//            throw new AuthenticationException("invalid token!");
//        }

        return JwtUtils.verify(accessToken) ? new SimpleAuthenticationInfo(JwtUtils.getClaims(accessToken), accessToken, getName()) : null;
    }
}
