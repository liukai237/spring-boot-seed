package com.iakuil.bf.common.domain;

import com.iakuil.bf.common.db.CommonSelfGenId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Entity基类
 *
 * <p>【强制】所有Table必须设计ID字段。
 * <p>在MyBatis框架下，所有的Entity都可以作为inDTO使用（但是不应该直接传递给前端）。
 * <p>同时包含单表分页、排序和查询过滤条件。
 *
 * @author Kai
 */
public class BaseEntity implements Pageable, Serializable {

    @Id
    @KeySql(genId = CommonSelfGenId.class)
    private Long id;

    @Transient
    private Integer pageSize;

    @Transient
    private Integer pageNum;

    @Transient
    private String orderBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public Integer getPageNum() {
        return pageNum;
    }

    @Override
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String getOrderBy() {
        return orderBy;
    }

    @Override
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
