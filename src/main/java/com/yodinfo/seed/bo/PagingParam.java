package com.yodinfo.seed.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页参数
 * Dao层方法使用此类作为参数，结果集将被分页
 */
@Getter
@Setter
@AllArgsConstructor
public class PagingParam {
    private Integer pageNum;
    private Integer pageSize;
    private String orderBy;
    private Object condition; // bean or map
}