package com.yodinfo.seed.constant;

import com.yodinfo.seed.util.BaseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GrantType implements BaseCodeEnum {
    CLIENT_CREDENTIALS(1, "client_credentials", "客户端授权模式"),
    AUTHORIZATION_CODE(2, "authorization_code", "授权码授权模式"),
    DEVICE_CODE(3, "device_code", "设备码授权模式"),
    REFRESH_TOKEN(4, "refresh_token", "刷新token");

    private Integer code;
    private String value;
    private String desc;
}