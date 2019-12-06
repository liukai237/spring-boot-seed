package com.yodinfo.seed.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationToken;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken implements AuthenticationToken, Serializable {
    private String uid;
    private String secret;

    public JwtToken(String secret) {
        this.secret = secret;
    }

    @Override
    public Object getPrincipal() {
        return uid;
    }

    @Override
    public Object getCredentials() {
        return secret;
    }
}
