package com.yodinfo.seed.service;

import com.yodinfo.seed.bo.TokenInfo;
import com.yodinfo.seed.constant.RedisKeyConstant;
import com.yodinfo.seed.exception.IllegalStatusException;
import com.yodinfo.seed.util.JwtUtils;
import com.yodinfo.seed.util.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * JWT服务
 */
@Slf4j
@Service
public class AuthTokenService {

    private static final long DEFAULT_ACCESS_EXPIRES = 2 * 60 * 60 * 1000; // 2 hours
    private static final long DEFAULT_REFRESH_EXPIRES = 7 * 24 * 60 * 60 * 1000; // 7 days

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public TokenInfo generate(String uid, Map<String, String> data) {
        String accessToken = JwtUtils.sign(uid, DEFAULT_ACCESS_EXPIRES, data);
        ValueOperations<String, Object> redis = redisTemplate.opsForValue();
        log.debug("generating access token: {}", accessToken);
        redis.set(RedisKeyConstant.JWT_ACCESS_TOKEN + uid, accessToken, DEFAULT_ACCESS_EXPIRES, TimeUnit.MILLISECONDS);

        String refreshToken = getRefreshTokenFromCache(uid);
        if (refreshToken == null || !JwtUtils.verify(refreshToken)) {
            refreshToken = JwtUtils.sign(uid, DEFAULT_REFRESH_EXPIRES);
            log.debug("generating refresh token: {}", refreshToken);
            redis.set(RedisKeyConstant.JWT_REFRESH_IDENTIFICATION + uid, refreshToken, DEFAULT_REFRESH_EXPIRES, TimeUnit.MILLISECONDS);
        }

        return new TokenInfo(accessToken, DEFAULT_ACCESS_EXPIRES / 1000, StrUtils.getMD5(refreshToken), null);
    }

    public TokenInfo refresh(String uid, String refreshTokenHash) {
        ValueOperations<String, Object> redis = redisTemplate.opsForValue();
        String accessToken = getAccessTokenFromCache(uid);
        if (accessToken == null) {
            log.info("missing access token for: {}", uid);
            throw new IllegalStatusException("missing access token", 100701);
        }

        String refreshToken = getRefreshTokenFromCache(uid);
        if (refreshToken == null) {
            throw new IllegalStatusException("missing refresh token", 100702);
        }
        if (!JwtUtils.verify(refreshToken) || !refreshTokenHash.equals(StrUtils.getMD5(refreshToken))) {
            throw new IllegalStatusException("invalid refresh token", 100703);
        }

        Map<String, String> data = JwtUtils.getClaims(accessToken);
        log.debug("stored data in token: {}", data);
        String newToken = JwtUtils.sign(uid, DEFAULT_ACCESS_EXPIRES, data);
        log.debug("generating access token: {}", newToken);
        redis.set(RedisKeyConstant.JWT_ACCESS_TOKEN + uid, newToken, DEFAULT_ACCESS_EXPIRES, TimeUnit.MILLISECONDS);
        return new TokenInfo(newToken, DEFAULT_ACCESS_EXPIRES / 1000, refreshTokenHash, null);
    }

    public String getAccessTokenFromCache(String uid) {
        ValueOperations<String, Object> redis = redisTemplate.opsForValue();
        Object accessObj = redis.get(RedisKeyConstant.JWT_ACCESS_TOKEN + uid);
        return accessObj == null ? null : (String) accessObj;
    }

    public String getRefreshTokenFromCache(String uid) {
        ValueOperations<String, Object> redis = redisTemplate.opsForValue();
        Object refreshObj = redis.get(RedisKeyConstant.JWT_REFRESH_IDENTIFICATION + uid);
        return refreshObj == null ? null : (String) refreshObj;
    }

    public void clear(String token) {
        String uid = JwtUtils.getIdentity(token);
        log.info("clearing token for: {}", uid);
        redisTemplate.delete(RedisKeyConstant.JWT_ACCESS_TOKEN + uid);
        redisTemplate.delete(RedisKeyConstant.JWT_REFRESH_IDENTIFICATION + uid);
    }
}
