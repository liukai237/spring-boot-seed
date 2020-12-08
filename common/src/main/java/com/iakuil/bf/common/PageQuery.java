package com.iakuil.bf.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iakuil.bf.common.tool.BeanMapUtils;

import java.util.Map;

/**
 * 数据库分页查询对象基类
 * <p>一般情况下不能够将Entity直接暴露为接口入参，也不建议使用Map，所以推荐所有<strong>分页查询参数</strong>均封装成JavaBean并且继承该类。</p><br/>
 * <p>当pagehelper.supportMethodsArguments配置为<strong>true</strong>时，可以实现自动分页排序。</p><br/>
 * <p>另外提供{@link #asMap}方法，以Map形式获取子类所有属性。</p>
 */
public abstract class PageQuery {

    @JsonIgnore
    private Integer pageSize;

    @JsonIgnore
    private Integer pageNum;

    @JsonIgnore
    private String orderBy;

    @JsonIgnore
    public Map<String, Object> asMap() {
        return BeanMapUtils.beanToMap(this);
    }

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