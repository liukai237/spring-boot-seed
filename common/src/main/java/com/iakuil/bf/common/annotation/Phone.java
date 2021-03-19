package com.iakuil.bf.common.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 手机号码校验
 * <p>
 * 三大运营商号码均可验证(不含卫星通信1349)
 * <br>　　　　　2018年3月已知
 * 中国电信号段
 * 133,149,153,173,177,180,181,189,199
 * 中国联通号段
 * 130,131,132,145,155,156,166,175,176,185,186
 * 中国移动号段
 * 134(0-8),135,136,137,138,139,147,150,151,152,157,158,159,178,182,183,184,187,188,198
 * 其他号段
 * 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
 * 虚拟运营商
 * 电信：1700,1701,1702
 * 移动：1703,1705,1706
 * 联通：1704,1707,1708,1709,171
 * 卫星通信：148(移动) 1349
 *
 * @author Kai
 * @date 2021/3/19 16:23
 **/
@Retention(RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@Pattern(regexp = "^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$")
public @interface Phone {
    String message() default "手机号码不正确，只支持大陆手机号码";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
