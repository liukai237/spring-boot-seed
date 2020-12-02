package com.iakuil.bf.service;

import com.iakuil.bf.service.config.WxSettings;
import com.iakuil.bf.service.constant.RedisConstant;
import com.iakuil.bf.service.constant.WxConstant;
import com.iakuil.bf.service.dto.wx.AccessToken;
import com.iakuil.bf.service.dto.wx.SessionKey;
import com.iakuil.bf.service.util.WxRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 小程序Token及SessionKey服务
 */
@Slf4j
@Service
public class WxTokenMgrService {

    private final WxSettings wxSettings;

    @Resource
    private WxRestTemplate restClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public WxTokenMgrService(WxSettings wxSettings) {
        this.wxSettings = wxSettings;
    }

    /**
     * 获取微信小程序Session Key
     * 缓存三天，其中UnionId可能为空
     */
    public SessionKey getSessionKeyByCode(String code) {
        SessionKey sessionKey = restClient.get(SessionKey.class, WxConstant.SESSION_KEY_URL, wxSettings.getAppid(), wxSettings.getAppsecret(), code);
        log.debug("--- sessionKey: [{}]", sessionKey);
        if (sessionKey != null) {
            redisTemplate.opsForValue().set(RedisConstant.MP_CACHE_SESSION_KEY_PREFIX + sessionKey.getOpenId(), sessionKey, 3, TimeUnit.DAYS);
        }

        return sessionKey;
    }

    /**
     * 获取AccessToken
     */
    public AccessToken getToken() {
        AccessToken token;
        Object cacheObj = redisTemplate.opsForValue().get(RedisConstant.MP_ACCESS_TOKEN_CACHE_KEY);
        if (cacheObj == null) {
            token = refreshToken();
        } else {
            AccessToken at = (AccessToken) cacheObj;
            Date expiresDate = DateUtils.addSeconds(at.getCreateTime(), at.getExpiresIn());
            if (new Date().before(expiresDate)) {
                token = at;
            } else {
                token = refreshToken();
            }
        }

        return token;
    }

    /**
     * 刷新AccessToken
     */
    public AccessToken refreshToken() {
        String appId = wxSettings.getAppid();
        log.info("Refreshing token for app [{}] and id [{}]", wxSettings.getAppid(), appId);
        AccessToken newToken = restClient.get(AccessToken.class, WxConstant.ACCESS_TOKEN_URL, appId, wxSettings.getAppsecret());
        newToken.setCreateTime(new Date());
        redisTemplate.opsForValue().set(RedisConstant.MP_ACCESS_TOKEN_CACHE_KEY, newToken, 7200, TimeUnit.SECONDS);
        log.debug("new token: {}", newToken.getAccessToken());
        return newToken;
    }
}
