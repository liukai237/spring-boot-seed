package com.yodinfo.seed.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class JwtUtils {

    private static final long DEFAULT_EXPIRE_TIME = 30 * 60 * 1000;
    private static final String DEFAULT_SECRET = "com.yodinfo.jwt.secret";
    private static final String DEFAULT_IDENTITY = "identity";

    public static String sign(String identityId) {
        return sign(identityId, DEFAULT_SECRET, DEFAULT_EXPIRE_TIME, Collections.emptyMap());
    }

    public static String sign(String identityId, long expire) {
        return sign(identityId, DEFAULT_SECRET, expire, Collections.emptyMap());
    }

    public static String sign(String identityId, long expire, Map<String, String> data) {
        return sign(identityId, DEFAULT_SECRET, expire, data);
    }

    public static String sign(String identityId, String secret, long expire, Map<String, String> data) {
        Objects.requireNonNull(identityId, "Id must not be empty!");
        Date date = new Date(System.currentTimeMillis() + expire);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create().withClaim(DEFAULT_IDENTITY, identityId);
        data.forEach(builder::withClaim);
        return builder.withExpiresAt(date).sign(algorithm);
    }

    public static boolean verify(String token) {
        return verify(token, DEFAULT_SECRET);
    }

    public static boolean verify(String token, String secret) {
        Objects.requireNonNull(token, "Token must not be empty!");
        String identityId = getIdentity(token);
        if (identityId == null) {
            return false;
        }

        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim(DEFAULT_IDENTITY, identityId)
                .build();
        try {
            verifier.verify(token);
        } catch (JWTDecodeException e) {
            log.error("expired token: {}", token);
            return false;
        }
        return true;
    }

    @Nullable
    public static String getIdentity(String token) {
        Objects.requireNonNull(token, "Token must not be empty!");
        String identity = null;
        try {
            DecodedJWT jwt = JWT.decode(token);
            identity = jwt.getClaim(DEFAULT_IDENTITY).asString();
            System.out.println();
        } catch (JWTDecodeException e) {
            log.error("invalid token: {}", token);
        }

        return identity;
    }

    public static Map<String, String> getClaims(String token) {
        Objects.requireNonNull(token, "Token must not be empty!");
        Map<String, String> claimMap = Maps.newHashMap();
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Claim> claims = jwt.getClaims();
            claims.forEach((k, v) -> {
                claimMap.put(k, v.asString());
            });
        } catch (JWTDecodeException e) {
            log.error("invalid token: {}", token);
        }

        return claimMap;
    }
}
