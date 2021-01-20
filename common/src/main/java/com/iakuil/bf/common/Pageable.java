package com.iakuil.bf.common;

import com.github.pagehelper.IPage;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 分页排序参数基类
 *
 * <p>pageSize、pageNum和orderBy三个属性仅用来配合PageHelper实现分页排序，没有其他业务含义。
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
