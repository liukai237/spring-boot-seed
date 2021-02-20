package com.iakuil.bf.common;

import lombok.*;

/**
 * 范围查询参数
 *
 * @author Kai
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RangeQuery {
    private String field;
    private Object lower;
    private Object upper;
    private Operator operator;

    public RangeQuery(String field, Object lower, Object upper) {
        this.field = field;
        this.lower = lower;
        this.upper = upper;
        // 默认不包含边界
        this.operator = Operator.GT_AND_LT;
    }

    /**
     * 范围取值运算符
     */
    public static enum Operator {
        GT_AND_LT, GTE_AND_LTE,GTE_AND_LT, GT_AND_LTE
    }
}