package com.iakuil.bf.common.annotation;


import com.iakuil.bf.common.validator.PassWordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 密码校验 注解
 *
 * @author zenglei
 * @date 2020-11-27
 */
@Constraint(validatedBy = PassWordValidator.class)
@Repeatable(Password.List.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Password {
    /**
     * 自定义正则
     *
     * @return String
     */
    String regexp() default "";

    /**
     * 自定义提示语
     *
     * @return String
     */
    String message() default "密码格式不正确，请输入8-20位的密码，必须包含数字和字母，支持特殊符号~!@#$%^*";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Password[] value();
    }

}
