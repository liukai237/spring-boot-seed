package com.iakuil.bf.common.constant;

import com.iakuil.bf.common.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender implements CodeEnum<Integer> {
    MALE(0, "男"),
    FEMALE(1,  "女"),
    OTHER(-1, "未知");

    private Integer value;
    private String name;
}