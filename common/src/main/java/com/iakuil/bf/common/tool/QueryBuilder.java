package com.iakuil.bf.common.tool;

import com.iakuil.bf.common.PageRequest;
import com.iakuil.bf.common.Pageable;
import com.iakuil.bf.common.constant.SysConstant;
import com.iakuil.toolkit.BeanMapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分页参数工具类
 *
 * @author Kai
 */
public class QueryBuilder {

    private Map<String, Object> params;

    private QueryBuilder() {
        this.params = new HashMap<>();
    }

    private QueryBuilder(PageRequest<?> pq) {
        PageRequest.Paging paging = pq.getPaging();
        PageRequest.Sorting[] sorting = pq.getSorting();
        String orderBy = null;
        if (sorting != null) {
            orderBy = Arrays.stream(sorting)
                    .filter(item -> StringUtils.isNoneBlank(item.getField()))
                    .map(item -> Strings.toUnderlineCase(item.getField()) + Strings.SPACE + item.getOrder().toString())
                    .collect(Collectors.joining(Strings.COMMA));
        }

        this.params = BeanMapUtils.beanToMap(pq.getFilter());
        this.params.put("pageNum", paging == null ? 1 : paging.getPageNum());
        this.params.put("pageSize", paging == null ? SysConstant.DEFAULT_PAGE_SIZE : paging.getPageSize());
        this.params.put("orderBy", orderBy);
    }

    public static QueryBuilder init() {
        return new QueryBuilder();
    }

    public static QueryBuilder init(PageRequest<?> pq) {
        return new QueryBuilder(pq);
    }

    public QueryBuilder pageSize(Integer pageSize) {
        this.params.put("pageNum", pageSize);
        return this;
    }

    public QueryBuilder pageNum(Integer pageNum) {
        this.params.put("pageNum", pageNum);
        return this;
    }

    public QueryBuilder orderBy(String orderBy) {
        this.params.put("pageNum", orderBy);
        return this;
    }

    public Map<String, Object> asMap() {
        return this.params;
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