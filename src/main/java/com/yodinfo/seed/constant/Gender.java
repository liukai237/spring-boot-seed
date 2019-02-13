package com.yodinfo.seed.constant;

import com.yodinfo.seed.BaseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum Gender implements BaseCodeEnum {
    MALE(0, "男"),
    FEMALE(1, "女"),
    OTHER(-1, "未知");

    private Integer code;
    private String desc;

    @Override
    public Integer getCode() {
        return code;
    }
}