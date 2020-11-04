package com.iakuil.seed.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页注解
 *
 * <p>方法体内第一条查询语句将会被分页，请不要与原生的PageHelper语句混合使用。</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface StartPage {

    int pageNum() default 1;

    int pageSize() default 0;

    String orderBy() default "";
}
