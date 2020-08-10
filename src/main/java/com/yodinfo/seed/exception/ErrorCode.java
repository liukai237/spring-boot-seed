package com.yodinfo.seed.exception;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义错误码
 * <p>用于参数校验时返回自定义错误码和错误描述。</p><br/>
 * <p>比如{@code @NotNull}校验默认返回统一的错误码，
 * 通过该注解可以针对手机号码不能空和姓名不能为空两种场景分别返回不同的错误码。</p><br/>
 * <p>该注解优先级高于其他普通校验注解。</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ErrorCode {

    @AliasFor("code")
    int value() default -1;

    String message() default "";
}
