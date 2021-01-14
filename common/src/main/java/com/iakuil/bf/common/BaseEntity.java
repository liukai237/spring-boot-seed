package com.iakuil.bf.common;

import javax.persistence.Transient;

/**
 * Entity基类
 * <p>【强制】所有Table必须设计ID字段。</p><br/>
 * <p>pageSize、pageNum和orderBy三个属性仅用来配合PageHelp实现分页排序，没有其他业务含义。</p>
 *
 * @author Kai
 */
public class BaseEntity extends BaseDomain {

    @Transient
    private Integer pageSize;

    @Transient
    private Integer pageNum;

    @Transient
    private String orderBy;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
