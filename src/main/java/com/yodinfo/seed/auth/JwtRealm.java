package com.yodinfo.seed.auth;

import com.yodinfo.seed.service.AuthTokenService;
import com.yodinfo.seed.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

@Slf4j
public class JwtRealm extends AuthorizingRealm {

    private AuthTokenService authTokenService;

    public JwtRealm(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String accessToken = (String) authenticationToken.getCredentials();
        String identityId = JwtUtils.getIdentity(accessToken);

        // 非最新
        String accessTokenInCache = authTokenService.getAccessTokenFromCache(identityId);
        if (accessTokenInCache == null || !accessTokenInCache.equals(accessToken)) {
            throw new AuthenticationException("invalid token!");
        }

        return new SimpleAuthenticationInfo(JwtUtils.getIdentity(accessToken), accessToken, "jwtRealm");
    }
}
