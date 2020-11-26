package com.iakuil.seed.common;

import com.google.common.util.concurrent.RateLimiter;
import com.iakuil.seed.annotation.LxRateLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;

/**
 * 基于接口的简易限流器
 * <p>基于IP的限流应该放在Nginx中。</p>
 */
@Aspect
@Slf4j
@Component
public class LxRateLimitAspect {
    private RateLimiter rateLimiter = RateLimiter.create(Double.MAX_VALUE);

    @Pointcut("@annotation(com.iakuil.seed.annotation.LxRateLimit)")
    public void checkPointcut() {
    }

    @ResponseBody
    @Around(value = "checkPointcut()")
    public Object aroundNotice(ProceedingJoinPoint pjp) throws Throwable {
        log.debug("拦截到了{}方法...", pjp.getSignature().getName());
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        if (targetMethod.isAnnotationPresent(LxRateLimit.class)) {
            LxRateLimit lxRateLimit = targetMethod.getAnnotation(LxRateLimit.class);
            rateLimiter.setRate(lxRateLimit.perSecond());
            if (!rateLimiter.tryAcquire(lxRateLimit.timeOut(), lxRateLimit.timeOutUnit())) {
                return new Resp(-1, "服务器繁忙，请稍后再试!");
            }
        }
        return pjp.proceed();
    }
}
