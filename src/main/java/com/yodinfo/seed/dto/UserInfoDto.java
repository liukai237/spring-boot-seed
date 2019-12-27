package com.yodinfo.seed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto implements Serializable {
    private Integer subscribe; // 0 means no
    @JsonProperty("openid")
    private String openId; // 未关注情况下只能获取到subscribe和openid
    @JsonProperty("nickname")
    private String nickName;
    private Integer sex;
    private String language;
    private String city;
    private String province;
    private String country;
    @JsonProperty("headimgurl")
    private String headImgUrl;
    @JsonProperty("subscribe_time")
    private Long subscribeTime;
    @JsonProperty("unionid")
    private String unionId;
    private String remark;
    @JsonProperty("groupid")
    private Integer groupId;
}