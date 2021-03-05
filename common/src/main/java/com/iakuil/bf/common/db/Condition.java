package com.iakuil.bf.common.db;

import com.github.pagehelper.IPage;
import com.google.common.collect.Sets;
import com.iakuil.bf.common.BaseEntity;
import com.iakuil.toolkit.BeanMapUtils;
import com.iakuil.toolkit.MapBuilder;
import tk.mybatis.mapper.entity.Example;

import java.util.Map;

/**
 * 可分页排序的条件查询
 *
 * <p>主要用于弥补通用查询方法不支持范围（模糊）查询。
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

    public static <T extends BaseEntity> Builder create(Class<T> entityClass) {
        return new Builder(entityClass);
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

    /**
     * Condition查询条件构造器
     *
     * <p>建议使用此工具类生成Condition，以保证Example复杂度可控。
     */
    public static class Builder {
        Condition condition;
        Condition.Criteria criteria;

        public Builder(Class<?> entityClass) {
            this.condition = new Condition(entityClass);
            this.criteria = condition.createCriteria();
        }

        public Builder with(Object param) {
            // 只填充非空的属性作为查询条件
            Map<String, Object> paramMap = MapBuilder.init(BeanMapUtils.beanToMap(param, true)).build();
            if (paramMap.isEmpty()) {
                // workaround, 如果查询条件为空，会多出一个and
                // see: https://github.com/abel533/Mapper/issues/817
                this.criteria.andCondition("1 = 1");
            } else {
                this.criteria.andAllEqualTo(paramMap);
            }
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.condition.setPageSize(pageSize);
            return this;
        }

        public Builder pageNum(Integer pageNum) {
            this.condition.setPageNum(pageNum);
            return this;
        }

        public Builder include(String... fields) {
            this.condition.selectProperties(fields);
            return this;
        }

        public Builder lt(String field, Object value) {
            this.criteria.andLessThan(field, value);
            return this;
        }

        public Builder lte(String field, Object value) {
            this.criteria.andLessThanOrEqualTo(field, value);
            return this;
        }

        public Builder gt(String field, Object value) {
            this.criteria.andGreaterThan(field, value);
            return this;
        }

        public Builder gte(String field, Object value) {
            this.criteria.andGreaterThanOrEqualTo(field, value);
            return this;
        }

        public Builder in(String field, Object... value) {
            this.criteria.andIn(field, Sets.newHashSet(value));
            return this;
        }

        public Builder like(String field, String keyword) {
            this.criteria.andLike(field, keyword + "%");
            return this;
        }

        public Builder orderByClause(String orderByClause) {
            // 注意：此处是a asc, b desc形式的SQL片段
            this.condition.setOrderByClause(orderByClause);
            return this;
        }

        public Condition build() {
            return this.condition;
        }
    }
}
