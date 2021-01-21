package com.iakuil.bf.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.bf.common.constant.SysConstant;
import com.iakuil.bf.common.tool.Strings;
import com.iakuil.toolkit.BeanMapUtils;
import com.iakuil.toolkit.BeanUtils;
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
 *
 * @param <T> 查询参数对象类型
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
     * 组装Query对象实现分页查询
     * <p>可以配合任何入参为Entity的通用方法实现单表分页查询。
     * <p>多表或者复杂的分页排序请在service层重新定义DTO继承Pageable。
     */
    @JsonIgnore
    public <R extends Pageable> R toQuery(Class<R> clazz) {
        R entity = BeanUtils.copy(this.filter, clazz);
        if (this.paging != null) {
            entity.setPageSize(this.paging.getPageSize());
            entity.setPageNum(this.paging.getPageNum());
        }

        if (this.sorting != null) {
            entity.setOrderBy(processSort());
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
        }

        if (this.sorting != null) {
            map.put("orderBy", processSort());
        }

        return map;
    }

    private String processSort() {
        return Arrays.stream(sorting)
                .filter(item -> StringUtils.isNoneBlank(item.getField()))
                .map(item -> Strings.toUnderlineCase(item.getField()) + Strings.SPACE + item.getOrder().toString())
                .collect(Collectors.joining(Strings.COMMA));
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
     * <p>合法pageSize范围：10到500
     */
    @Getter
    @Setter
    @ApiModel(value = "Paging", description = "分页参数")
    public static class Paging {
        @ApiModelProperty(name = SysConstant.DEFAULT_PAGE_NUM_FIELD, value = "分页页码", example = "1")
        @JsonProperty(SysConstant.DEFAULT_PAGE_NUM_FIELD)
        private Integer pageNum;
        @ApiModelProperty(name = SysConstant.DEFAULT_PAGE_SIZE_FIELD, value = "分页尺寸", example = "10")
        @JsonProperty(SysConstant.DEFAULT_PAGE_SIZE_FIELD)
        private Integer pageSize;

        public Integer getPageSize() {
            return (pageSize == null || pageSize < 10) ? SysConstant.DEFAULT_PAGE_SIZE : (pageSize > SysConstant.MAX_PAGE_SIZE ? SysConstant.MAX_PAGE_SIZE : pageSize);
        }
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