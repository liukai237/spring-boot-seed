package com.iakuil.bf.common;

import com.github.pagehelper.IPage;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 分页
 *
 * @author Kai
 */
public abstract class Pageable implements IPage, Serializable {

    @Transient
    private Integer pageSize;

    @Transient
    private Integer pageNum;

    @Transient
    private String orderBy;

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
