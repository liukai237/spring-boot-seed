package com.yodinfo.seed.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信公众平台Token
 */
@Getter
@Setter
@ToString
public class AccessToken implements Serializable {

    @JsonProperty("access_token")
    protected String accessToken;

    @JsonProperty("expires_in")
    protected Integer expiresIn;

    @JsonProperty("create_time")
    protected Date createTime; // 自定义字段，用来判断token是否过期
}