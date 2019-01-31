package com.yodinfo.seed.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE(0, "男"),
    FEMALE(1, "女"),
    OTHER(-1, "未知");

    private Integer code;
    private String desc;

    public static Gender getByCode(Integer code) {
        for (Gender gender : Gender.values()) {
            if (Objects.equals(code, gender.code)) {
                return gender;
            }
        }

        return OTHER;
    }
}