package com.iakuil.bf.common.tool;

import com.iakuil.bf.common.BaseEntity;
import com.iakuil.bf.common.PageRequest;
import com.iakuil.bf.common.Pageable;
import com.iakuil.bf.common.constant.SysConstant;
import com.iakuil.bf.common.db.Condition;
import com.iakuil.toolkit.BeanMapUtils;
import com.iakuil.toolkit.MapBuilder;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分页参数工具类
 *
 * <p>用来新建可分页排序的查询对象，比如Entity和{@link Condition}
 *
 * @author Kai
 */
public class QueryBuilder {

    private Map<String, Object> params;

    private QueryBuilder() {
        this.params = new HashMap<>();
    }

    public static QueryBuilder init() {
        return new QueryBuilder();
    }

    public static QueryBuilder init(BaseEntity entity) {
        QueryBuilder qb = new QueryBuilder();
        qb.params = BeanMapUtils.beanToMap(entity);
        return qb;
    }

    public static QueryBuilder init(PageRequest<?> pq) {
        QueryBuilder qb = new QueryBuilder();
        qb.params = BeanMapUtils.beanToMap(pq.getFilter());

        PageRequest.Paging paging = pq.getPaging();
        PageRequest.Sorting[] sorting = pq.getSorting();
        String orderBy;
        if (sorting != null) {
            // 驼峰转下划线，逗号分隔
            orderBy = Arrays.stream(sorting)
                    .filter(item -> StringUtils.isNoneBlank(item.getField()))
                    .map(item -> Strings.toUnderlineCase(item.getField()) + Strings.SPACE + item.getOrder().toString())
                    .collect(Collectors.joining(Strings.COMMA));
            qb.params.put(SysConstant.DEFAULT_SORT_FIELD, orderBy);
        }
        if (paging != null) {
            Integer pageSize = ObjectUtils.defaultIfNull(paging.getPageSize(), SysConstant.DEFAULT_PAGE_SIZE);
            qb.params.put(SysConstant.DEFAULT_PAGE_SIZE_FIELD, pageSize > SysConstant.MAX_PAGE_SIZE ? SysConstant.MAX_PAGE_SIZE : pageSize);
            qb.params.put(SysConstant.DEFAULT_PAGE_NUM_FIELD, ObjectUtils.defaultIfNull(paging.getPageNum(), 1));
        }
        return qb;
    }

    public QueryBuilder pageSize(Integer pageSize) {
        this.params.put(SysConstant.DEFAULT_PAGE_SIZE_FIELD, pageSize);
        return this;
    }

    public QueryBuilder pageNum(Integer pageNum) {
        this.params.put(SysConstant.DEFAULT_PAGE_NUM_FIELD, pageNum);
        return this;
    }

    public QueryBuilder orderBy(String orderBy) {
        this.params.put(SysConstant.DEFAULT_SORT_FIELD, orderBy);
        return this;
    }

    public Map<String, Object> asMap() {
        return this.params;
    }

    public Condition asCondition(Class<?> clazz) {
        Condition condition = new Condition(clazz);
        Condition.Criteria criteria = condition.createCriteria();
        criteria.andAllEqualTo(MapBuilder.init(this.params).build());
        condition.setPageNum(MapUtils.getInteger(this.params, SysConstant.DEFAULT_PAGE_NUM_FIELD));
        condition.setPageSize(MapUtils.getInteger(this.params, SysConstant.DEFAULT_PAGE_SIZE_FIELD));
        condition.setOrderByClause(MapUtils.getString(this.params, SysConstant.DEFAULT_SORT_FIELD));
        return condition;
    }

    public <R extends Pageable> R build(Class<R> clazz) {
        R target;
        try {
            target = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Occurring an exception during class instancing!", e);
        }
        BeanMapUtils.mapToBean(this.params, target);
        return target;
    }
}
