package com.yodinfo.seed.service;

import com.yodinfo.seed.bo.AuthToken;
import com.yodinfo.seed.constant.RedisKeyConstant;
import com.yodinfo.seed.exception.IllegalStatusException;
import com.yodinfo.seed.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthTokenService {

    private static final String MD5_SALT = "itsASaltForRefreshToken251";
    private static final long DEFAULT_ACCESS_EXPIRES = 2 * 60 * 60 * 1000; // 2 hours
    private static final long DEFAULT_REFRESH_EXPIRES = 7 * 24 * 60 * 60 * 1000; // 7 days

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public AuthToken generate(String uid, Map<String, String> data) {
        String accessToken = JwtUtils.sign(uid, DEFAULT_ACCESS_EXPIRES, data);
        ValueOperations<String, Object> redis = redisTemplate.opsForValue();
        log.info("generating access token: {}", accessToken);
        redis.set(RedisKeyConstant.JWT_ACCESS_TOKEN + uid, accessToken, DEFAULT_ACCESS_EXPIRES, TimeUnit.MILLISECONDS);

        Object tokenObj = redis.get(RedisKeyConstant.JWT_REFRESH_IDENTIFICATION + uid);
        String refreshToken;
        if (tokenObj == null || !JwtUtils.verify((String) tokenObj)) {
            refreshToken = JwtUtils.sign(uid, DEFAULT_REFRESH_EXPIRES);
            log.info("generating refresh token: {}", refreshToken);
            redis.set(RedisKeyConstant.JWT_REFRESH_IDENTIFICATION + uid, refreshToken, DEFAULT_REFRESH_EXPIRES, TimeUnit.MILLISECONDS);
        } else {
            refreshToken = ((String) tokenObj);
        }

        return new AuthToken(accessToken, DEFAULT_ACCESS_EXPIRES / 1000, getMD5(refreshToken), null);
    }

    public AuthToken refresh(String uid, String refreshTokenHash) {
        ValueOperations<String, Object> redis = redisTemplate.opsForValue();
        Object accessObj = redis.get(RedisKeyConstant.JWT_REFRESH_IDENTIFICATION + uid);
        if (accessObj == null) {
            log.info("missing access token for: {}", uid);
            throw new IllegalStatusException("missing access token", 100701);
        }

        Object refreshObj = redis.get(RedisKeyConstant.JWT_REFRESH_IDENTIFICATION + uid);
        if (refreshObj == null) {
            throw new IllegalStatusException("missing refresh token", 100702);
        }
        String refreshToken = (String) refreshObj;
        if (!JwtUtils.verify(refreshToken) || !refreshTokenHash.equals(getMD5(refreshToken))) {
            throw new IllegalStatusException("invalid refresh token", 100703);
        }

        Map<String, String> data = JwtUtils.getClaims((String) accessObj);
        log.debug("stored data in token: {}", data);
        String accessToken = JwtUtils.sign(uid, DEFAULT_ACCESS_EXPIRES, data);
        log.info("generating access token: {}", accessToken);
        redis.set(RedisKeyConstant.JWT_ACCESS_TOKEN + uid, accessToken, DEFAULT_ACCESS_EXPIRES, TimeUnit.MILLISECONDS);
        return new AuthToken(accessToken, DEFAULT_ACCESS_EXPIRES / 1000, refreshTokenHash, null);
    }

    public void clear(String token) {
        String uid = JwtUtils.getIdentity(token);
        log.info("clearing token for: {}", uid);
        redisTemplate.delete(RedisKeyConstant.JWT_ACCESS_TOKEN + uid);
        redisTemplate.delete(RedisKeyConstant.JWT_REFRESH_IDENTIFICATION + uid);
    }

    private String getMD5(String str) {
        String base = str + "/" + MD5_SALT;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
