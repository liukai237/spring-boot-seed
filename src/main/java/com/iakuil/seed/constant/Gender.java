package com.iakuil.seed.constant;

import com.iakuil.seed.support.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender implements CodeEnum {
    MALE(0, "male", "男"),
    FEMALE(1, "female", "女"),
    OTHER(-1, "other", "未知");

    private Integer code;
    private String label;
    private String desc;
}