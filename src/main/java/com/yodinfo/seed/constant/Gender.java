package com.yodinfo.seed.constant;

import com.yodinfo.seed.util.BaseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender implements BaseCodeEnum {
    MALE(0, "male", "男"),
    FEMALE(1, "female", "女"),
    OTHER(-1, "other", "未知");

    private Integer code;
    private String value;
    private String desc;
}