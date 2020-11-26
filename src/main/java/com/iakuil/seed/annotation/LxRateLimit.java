package com.iakuil.seed.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 接口限流
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LxRateLimit {

    String value() default "";

    /**
     * 每秒向桶中放入令牌的数量
     * 默认最大即不做限流
     */
    double perSecond() default Double.MAX_VALUE;

    /**
     * 获取令牌的超时时间
     * 默认0
     */
    int timeOut() default 0;

    /**
     * 超时时间单位
     */
    TimeUnit timeOutUnit() default TimeUnit.MILLISECONDS;
}
