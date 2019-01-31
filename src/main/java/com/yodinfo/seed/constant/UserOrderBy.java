package com.yodinfo.seed.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserOrderBy {
    REG_TIME_ASC("create_time asc"),
    REG_TIME_DESC("create_time desc");

    private String realValue;
}
