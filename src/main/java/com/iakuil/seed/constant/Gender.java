package com.iakuil.seed.constant;

import com.iakuil.seed.common.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender implements CodeEnum {
    MALE(0, "男"),
    FEMALE(1,  "女"),
    OTHER(-1, "未知");

    private Integer code;
    private String desc;
}