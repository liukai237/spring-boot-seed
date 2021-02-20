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
}