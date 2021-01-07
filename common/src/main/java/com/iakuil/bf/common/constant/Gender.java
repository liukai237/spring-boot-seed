package com.iakuil.bf.common.constant;

import com.iakuil.bf.common.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author Kai
 */

@Getter
@AllArgsConstructor
public enum Gender implements DictEnum<Integer> {
    MALE(0, "男"),
    FEMALE(1,  "女"),
    OTHER(-1, "未知");

    private Integer value;
    private String name;

    @Override
    public String description() {
        return "性别";
    }
}