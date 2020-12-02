package com.iakuil.bf.service.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.bf.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 微信公众平台Token
 */
@Getter
@Setter
@ToString
public class AccessToken extends BaseDomain {

    @JsonProperty("access_token")
    protected String accessToken;

    @JsonProperty("expires_in")
    protected Integer expiresIn;

    @JsonProperty("create_time")
    protected Date createTime; // 自定义字段，用来判断token是否过期
}