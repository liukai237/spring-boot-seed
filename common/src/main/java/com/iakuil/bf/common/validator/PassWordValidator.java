package com.iakuil.bf.common.validator;

import com.iakuil.bf.common.annotation.Password;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 密码校验 validator
 *
 * @author zenglei
 * @date 2020-11-27
 */
public class PassWordValidator implements ConstraintValidator<Password, String> {
    /**
     * 默认正则
     */
    private String regexp = "^(?![0-9]+$)(?![a-zA-Z~!@#$%^*]+$)[0-9A-Za-z~!@#$%^*]{8,20}$";

    @Override
    public void initialize(Password constraintAnnotation) {
        // 是否自定义了正则
        if (StringUtils.isNotBlank(constraintAnnotation.regexp())) {
            this.regexp = constraintAnnotation.regexp();
        }
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 校验
        return !StringUtils.isBlank(s) && s.matches(regexp);
    }
}
