package com.iakuil.bf.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.bf.common.constant.CommonConstant;
import com.iakuil.bf.common.tool.Strings;
import com.iakuil.toolkit.BeanMapUtils;
import com.iakuil.toolkit.BeanUtils;
import com.sun.javafx.collections.MappingChange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据库分页排序查询请求体
 * <p>当pagehelper.supportMethodsArguments配置为<strong>true</strong>时，可以实现自动分页排序。</p>
 *
 * @author Kai
 */
@ApiModel(value = "PageQuery", description = "统一封装的分页排序请求体")
public class PageQuery<T> {

    @ApiModelProperty(name = "filtering", value = "过滤参数。")
    private T filter;

    @ApiModelProperty(value = "paging", notes = "分页参数。")
    private Paging paging;

    @ApiModelProperty(value = "sorting", notes = "排序参数。")
    private Sorting[] sorting;

    /**
     * 组装Pageable实现分页查询
     * <p>可以配合任何入参为Entity的通用方法实现单表分页查询。
     * <p>多表或者复杂的分页排序请在sevice层重新定义DTO继承Pageable。
     */
    @JsonIgnore
    public <R extends Pageable> R toPage(Class<R> clazz) {
        R entity = BeanUtils.copy(this.filter, clazz);
        if (this.paging != null) {
            entity.setPageSize(this.paging.getPageSize());
            entity.setPageNum(this.paging.getPageNum());
        } else {
            entity.setPageSize(0); // zero means no paging
            entity.setPageNum(1);
        }

        if (this.sorting != null) {
            entity.setOrderBy(Arrays.stream(sorting)
                    .filter(item -> StringUtils.isNoneBlank(item.getField()))
                    .map(item -> Strings.toUnderlineCase(item.getField()) + " " + item.getOrder().toString())
                    .collect(Collectors.joining()));
        }

        return entity;
    }

    /**
     * 组装Map查询参数
     * <p>可以配合selectMap通用方法使用。
     * <p>用于遗留接口分页查询，单表、多表皆可使用。
     */
    @JsonIgnore
    public Map<String, Object> toMap() {
        Map<String, Object> map = BeanMapUtils.beanToMap(this.filter);
        if (this.paging != null) {
            map.put("pageSize", this.paging.getPageSize());
            map.put("pageNum", this.paging.getPageNum());
        } else {
            // zero means no paging
            map.put("pageSize", 0);
            map.put("pageNum", 1);
        }

        if (this.sorting != null) {
            map.put("orderBy", Arrays.stream(sorting)
                    .filter(item -> StringUtils.isNoneBlank(item.getField()))
                    .map(item -> Strings.toUnderlineCase(item.getField()) + " " + item.getOrder().toString())
                    .collect(Collectors.joining()));
        }

        return map;
    }

    public T getFilter() {
        return filter;
    }

    public void setFilter(T filter) {
        this.filter = filter;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public void setSorting(Sorting[] sorting) {
        this.sorting = sorting;
    }

    /**
     * 分页参数
     * <p>合法pageSize范围：0到500</p>
     */
    @Getter
    @Setter
    @ApiModel(value = "Paging", description = "分页参数")
    public static class Paging {
        @ApiModelProperty(name = CommonConstant.DEFAULT_PAGE_NUM_FIELD, value = "分页页码", example = "1")
        @JsonProperty(CommonConstant.DEFAULT_PAGE_NUM_FIELD)
        private Integer pageNum;
        @ApiModelProperty(name = CommonConstant.DEFAULT_PAGE_SIZE_FIELD, value = "分页尺寸", example = "10")
        @JsonProperty(CommonConstant.DEFAULT_PAGE_SIZE_FIELD)
        private Integer pageSize;
    }

    /**
     * 排序参数
     */
    @Getter
    @Setter
    @ApiModel(value = "Sorting", description = "排序参数")
    public static class Sorting {
        @ApiModelProperty(name = "field", value = "排序字段", example = "createTime")
        private String field;
        @ApiModelProperty(name = "order", value = "排序方向", allowableValues = "asc,desc", example = "desc")
        private Direction order;
    }

    @Getter
    public enum Direction {
        ASC, DESC
    }
}