package com.iakuil.bf.service.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.bf.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SessionKey extends BaseDomain {
    @JsonProperty("openid")
    private String openId;
    @JsonProperty("session_key")
    private String sessionKey;
    @JsonProperty("unionid")
    private String unionId;
}