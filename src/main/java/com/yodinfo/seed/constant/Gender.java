package com.yodinfo.seed.constant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yodinfo.seed.BaseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum Gender implements BaseCodeEnum {
    @JsonProperty("male")
    MALE(0, "男"),
    @JsonProperty("female")
    FEMALE(1, "女"),
    @JsonProperty("other")
    OTHER(-1, "未知");

    private Integer code;
    private String desc;

    @Override
    public Integer getCode() {
        return code;
    }
}