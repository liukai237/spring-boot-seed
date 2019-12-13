package com.yodinfo.seed.auth;

import com.yodinfo.seed.constant.GrantType;
import com.yodinfo.seed.constant.OAuth2Platform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationToken;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Token implements AuthenticationToken {
    private OAuth2Platform platform;
    private GrantType grantType;
    private String appId; // app_id or client_id
    private String secret; // 密码/秘钥
    private String code;

    public OAuth2Token(String appId, String secret, String code) {
        this.appId = appId;
        this.secret = secret;
        this.code = code;
    }

    public OAuth2Platform getPlatform() {
        return platform;
    }

    public void setPlatform(OAuth2Platform platform) {
        this.platform = platform;
    }

    public GrantType getGrantType() {
        return grantType;
    }

    public void setGrantType(GrantType grantType) {
        this.grantType = grantType;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
