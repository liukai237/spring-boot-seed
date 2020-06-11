package com.yodinfo.seed.dto.wx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MpUserInfoDTO {
    private String openId;
    private String nickName;
    private Integer gender;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    private String unionId;
    private Watermark watermark;
}
