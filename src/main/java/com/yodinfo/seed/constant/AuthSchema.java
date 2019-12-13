package com.yodinfo.seed.constant;

import com.yodinfo.seed.util.BaseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthSchema implements BaseCodeEnum {
    PASSWD(1, "passwd", "用户名密码"),
    SMS(2, "sms", "短信验证码"),
    OAUTH2(3, "auth_code", "第三方OAuth2.0");

    private Integer code;
    private String value;
    private String desc;
}
