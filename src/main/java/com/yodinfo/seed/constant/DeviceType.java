package com.yodinfo.seed.constant;

import com.yodinfo.seed.util.BaseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceType implements BaseCodeEnum {
    WEBAPP(1, "webapp", "网页端"),
    IOS(2, "ios", "苹果设备"),
    ADROID(3, "android", "安卓设备"),
    WXOA(4, "wxoa", "微信公众号"),
    WXMP(5, "wxmp", "微信小程序");

    private Integer code;
    private String value;
    private String desc;
}
