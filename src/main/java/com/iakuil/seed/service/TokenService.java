package com.iakuil.seed.service;

import com.baomidou.kaptcha.Kaptcha;
import com.iakuil.seed.constant.RespCode;
import com.iakuil.seed.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenService {

    private static final String TOKEN_KEY = "tk:key:";

    private static final String SMS_CODE_KEY = "tk:sms:";

    private static final String DEFAULT_PLACEHOLDER = "-";

    private static final long DEFAULT_TIMEOUT = 5L;

    private static final long CAPTCHA_TIMEOUT = 60 * 5;

    private static final long SMS_TIMEOUT = 60;

    @Resource
    private Kaptcha kaptcha;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public String getSimpleToken() {
        return getSimpleToken(DEFAULT_TIMEOUT, TimeUnit.MINUTES);
    }

    public String getSimpleToken(final long timeout, final TimeUnit unit) {
        String token = RandomStringUtils.random(40, true, true);
        if (timeout > 0) {
            stringRedisTemplate.opsForValue().set(TOKEN_KEY + token, DEFAULT_PLACEHOLDER, timeout, unit);
        } else {
            stringRedisTemplate.opsForValue().set(TOKEN_KEY + token, DEFAULT_PLACEHOLDER, DEFAULT_TIMEOUT, TimeUnit.MINUTES);
        }
        return token;
    }

    public void checkSimple(String token) {
        String data = stringRedisTemplate.opsForValue().get(TOKEN_KEY + token);
        stringRedisTemplate.delete(TOKEN_KEY + token);
        if (data == null) {
            throw new BusinessException(RespCode.INVALID_TOKEN.getMessage(), RespCode.INVALID_TOKEN.getCode());
        }
    }

    public void renderCaptcha() {
        kaptcha.render();
    }

    public boolean validCaptcha(String code) {
        return kaptcha.validate(code, CAPTCHA_TIMEOUT);
    }

    public boolean validCaptcha(String code, long sec) {
        return kaptcha.validate(code, sec);
    }

    public void sendSmsCode(String tel) {
        //TODO 对接短信服务商
        stringRedisTemplate.opsForValue().set(SMS_CODE_KEY + tel, "123456", SMS_TIMEOUT, TimeUnit.SECONDS);
    }

    public void verifySmsCode(String tel, String code) {
        String cached = stringRedisTemplate.opsForValue().get(SMS_CODE_KEY + tel);
        if (cached == null || !cached.equalsIgnoreCase(code)) {
            stringRedisTemplate.delete(SMS_CODE_KEY + tel);
            throw new BusinessException(RespCode.INVALID_SMS_CODE.getMessage(), RespCode.INVALID_SMS_CODE.getCode());
        }
    }
}
