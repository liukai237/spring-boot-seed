package com.iakuil.bf.common.db;

import com.github.pagehelper.IPage;
import tk.mybatis.mapper.entity.Example;

/**
 * 可分页排序的条件查询
 *
 * @author Kai
 */
public class Condition extends Example implements IPage {
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

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String getOrderBy() {
        return super.orderByClause;
    }

    public void setOrderBy(String orderBy) {
        super.setOrderByClause(orderBy);
    }
}
