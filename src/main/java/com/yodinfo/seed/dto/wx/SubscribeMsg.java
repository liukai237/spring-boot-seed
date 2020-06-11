package com.yodinfo.seed.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 订阅消息
 */
@Getter
@Setter
public class SubscribeMsg {
    private String touser;
    @JsonProperty("template_id")
    private String templateId;
    private String page;
    private Map<String, Object> data;
    @JsonProperty("miniprogram_state")
    private String miniProgramState;
    private String lang;
}
