package com.iakuil.bf.common.db;

import com.iakuil.bf.common.Pageable;
import tk.mybatis.mapper.entity.Example;

/**
 * 可分页排序的条件查询
 *
 * <p>主要用于范围（模糊）查询，请勿滥用对象查询。
 *
 * @author Kai
 */
public class Condition extends Example implements Pageable {

    private Integer pageNum;

    private Integer pageSize;

    public Condition(Class<?> entityClass) {
        super(entityClass);
    }

    public Condition(Class<?> entityClass, boolean exists) {
        super(entityClass, exists);
    }

    public Condition(Class<?> entityClass, boolean exists, boolean notNull) {
        super(entityClass, exists, notNull);
    }

    @Override
    public Integer getPageNum() {
        return pageNum;
    }

    @Override
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String getOrderBy() {
        return super.orderByClause;
    }

    @Override
    public void setOrderBy(String orderBy) {
        super.setOrderByClause(orderBy);
    }
}
