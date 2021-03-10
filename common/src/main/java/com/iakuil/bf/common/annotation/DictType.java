package com.iakuil.bf.common.annotation;

import java.lang.annotation.*;

/**
 * 数据字典注解
 *
 * <p>将属性标注为数据字典。
 *
 * @author Kai
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DictType {
    String value() default "";
}
