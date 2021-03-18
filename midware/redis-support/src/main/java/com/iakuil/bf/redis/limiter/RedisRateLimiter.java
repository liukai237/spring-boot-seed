package com.iakuil.bf.redis.limiter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * 限流切面
 */
@Slf4j
@Aspect
@Configuration
public class RedisRateLimiter {

    @Autowired
    private RedisTemplate<String, Object> limitRedisTemplate;

    @Autowired
    private RedisScript<Number> limitRateScript;

    @Around("execution(public * *(..)) && @annotation(com.iakuil.bf.redis.limiter.Limit)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnnotation.limitType();
        String name = limitAnnotation.name();
        String key;
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();

        // 根据限流类型获取不同的key，不传则以方法名作为key
        switch (limitType) {
            case IP:
                key = getIpAddress();
                break;
            case CUSTOMER:
                key = limitAnnotation.key();
                if (StringUtils.isBlank(key)) {
                    key = StringUtils.upperCase(method.getName());
                }
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }

        List<String> keys = Collections.singletonList(StringUtils.join(limitAnnotation.prefix(), key));
        Number count = limitRedisTemplate.execute(limitRateScript, keys, limitCount, limitPeriod);
        log.info("Access try count is {} for name={} and key = {}", count, name, key);
        if (count != null && count.intValue() <= limitCount) {
            return pjp.proceed();
        } else {
            throw new IllegalStateException("服务器繁忙，请稍后再试！");
        }
    }

    /**
     * 获取id地址
     */
    public String getIpAddress() {
        return "127.0.0.1";
    }
}
