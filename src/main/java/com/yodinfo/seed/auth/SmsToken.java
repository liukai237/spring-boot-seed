package com.yodinfo.seed.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationToken;

import java.io.Serializable;

/**
 * JWT Shiro Token
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsToken implements AuthenticationToken, Serializable {
    private String uid;
    private String ticket; // 图形验证码等
    private String code; // 短信验证码

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
