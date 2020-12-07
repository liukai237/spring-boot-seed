package com.iakuil.bf.common.annotation;

import java.lang.annotation.*;

/**
 * 数据字典注解
 * <p>将属性标注为数据字典。默认使用属性名称作为value（驼峰转下划线）。</p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DictType {
    String value() default "";
}
