package com.iakuil.bf.common.tool;

import com.iakuil.bf.common.BaseEntity;
import com.iakuil.bf.common.db.Condition;
import com.iakuil.toolkit.BeanMapUtils;
import com.iakuil.toolkit.MapBuilder;

import java.util.Map;

/**
 * Condition查询条件构造器
 *
 * <p>建议使用此工具类生成Condition，以保证Example复杂度可控。
 *
 * @author Kai
 */
public class ConditionBuilder {
    Condition condition;
    Condition.Criteria criteria;

    private ConditionBuilder() {
    }

    private ConditionBuilder(Class<?> entityClass) {
        this.condition = new Condition(entityClass);
        this.criteria = condition.createCriteria();
    }

    public static ConditionBuilder init(BaseEntity entity) {
        return init(entity, entity.getClass());
    }

    public static ConditionBuilder init(Class<? extends BaseEntity> entityClass) {
        ConditionBuilder builder = new ConditionBuilder(entityClass);
        // workaround，没有criteria时会多出一个and
        builder.criteria.andIsNotNull("id");
        return builder;
    }

    public static ConditionBuilder init(Object param, Class<? extends BaseEntity> entityClass) {
        ConditionBuilder qb = new ConditionBuilder(entityClass);
        Map<String, Object> paramMap = MapBuilder.init(BeanMapUtils.beanToMap(param, true)).build();
        // 只填充非空的属性作为查询条件
        if (paramMap.isEmpty()) {
            qb.criteria.andIsNotNull("id");
        } else {
            qb.criteria.andAllEqualTo(paramMap);
        }
        return qb;
    }

    public ConditionBuilder pageSize(Integer pageSize) {
        this.condition.setPageSize(pageSize);
        return this;
    }

    public ConditionBuilder pageNum(Integer pageNum) {
        this.condition.setPageNum(pageNum);
        return this;
    }

    public ConditionBuilder include(String... fields) {
        this.condition.selectProperties(fields);
        return this;
    }

    public ConditionBuilder lt(String field, Object value) {
        this.criteria.andLessThan(field, value);
        return this;
    }

    public ConditionBuilder lte(String field, Object value) {
        this.criteria.andLessThanOrEqualTo(field, value);
        return this;
    }

    public ConditionBuilder gt(String field, Object value) {
        this.criteria.andGreaterThan(field, value);
        return this;
    }

    public ConditionBuilder gte(String field, Object value) {
        this.criteria.andGreaterThanOrEqualTo(field, value);
        return this;
    }

    public ConditionBuilder orderByClause(String orderByClause) {
        // 注意：此处是a asc, b desc形式的片段
        this.condition.setOrderByClause(orderByClause);
        return this;
    }

    public Condition build() {
        return this.condition;
    }
}
