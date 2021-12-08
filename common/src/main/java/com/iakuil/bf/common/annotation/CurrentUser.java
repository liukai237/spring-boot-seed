package com.iakuil.bf.common.annotation;

import java.lang.annotation.*;

/**
 * 当前登录用户
 *
 * @author Kai
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}