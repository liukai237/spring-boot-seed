package com.iakuil.seed.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Watermark implements Serializable {
    private Long timestamp;
    @JsonProperty("appid")
    private String appId;
}
