package com.yodinfo.seed.bo;

import io.swagger.annotations.ApiModelProperty;

public class AbstractReq<T> {
    @ApiModelProperty(name = "filter", value = "过滤参数")
    private T filter;

    public T getFilter() {
        return filter;
    }

    public void setFilter(T filter) {
        this.filter = filter;
    }
}