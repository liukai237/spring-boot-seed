package com.iakuil.bf.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.bf.common.constant.SysConstant;
import com.iakuil.bf.common.db.Condition;
import com.iakuil.bf.common.tool.Strings;
import com.iakuil.toolkit.BeanUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 数据库分页排序查询请求体
 *
 * @param <T> 查询参数对象类型
 * @author Kai
 */
@ApiModel(value = "PageRequest", description = "统一封装的分页排序请求体")
public class PageRequest<T> {

    @ApiModelProperty(name = "filtering", value = "过滤参数。")
    private T filter;

    @ApiModelProperty(value = "paging", notes = "分页参数。")
    private Paging paging;

    @ApiModelProperty(value = "sorting", notes = "排序参数。")
    private Sorting[] sorting;

    /**
     * 转换为Query对象，并填充分页排序属性
     *
     * <p>如果是单表通用查询，一般转换为Entity对象；如果是复杂查询，Query对象应该继承{@code Pageable}。
     */
    public <R extends Pageable> R asQuery(Class<R> clazz) {
        R params = BeanUtils.copy(this.getFilter(), clazz);
        PageRequest.Paging paging = this.getPaging();
        PageRequest.Sorting[] sorting = this.getSorting();
        String orderBy;
        if (sorting != null) {
            // 驼峰转下划线，逗号分隔
            orderBy = Arrays.stream(sorting)
                    .filter(item -> StringUtils.isNoneBlank(item.getField()))
                    .map(item -> Strings.toUnderlineCase(item.getField()) + Strings.SPACE + item.getOrder().toString())
                    .collect(Collectors.joining(Strings.COMMA));
            params.setOrderBy(orderBy);
        }
        if (paging != null) {
            Integer pageSize = ObjectUtils.defaultIfNull(paging.getPageSize(), SysConstant.DEFAULT_PAGE_SIZE);
            params.setPageSize(pageSize > SysConstant.MAX_PAGE_SIZE ? SysConstant.MAX_PAGE_SIZE : pageSize);
            params.setPageNum(ObjectUtils.defaultIfNull(paging.getPageNum(), 1));
        }
        return params;
    }

    /**
     * 转换为Condition对象，并填充分页排序属性
     *
     * <p>只有存在于Entity的属性才能作为查询条件。
     */
    public <R extends BaseEntity> Condition asCondition(Class<R> clazz) {
        // 将非空的查询参数复制到Entity对象。
        Condition.Builder cb = Condition.create(clazz).with(this.getFilter());
        if (this.getPaging() != null) {
            cb.pageNum(this.getPaging().getPageNum()).pageSize(this.getPaging().getPageSize());
        }
        if (this.getSorting() != null) {
            // 驼峰转下划线，逗号分隔
            cb.orderByClause(Arrays.stream(this.getSorting())
                    .filter(item -> StringUtils.isNoneBlank(item.getField()))
                    .map(item -> Strings.toUnderlineCase(item.getField()) + Strings.SPACE + item.getOrder().toString())
                    .collect(Collectors.joining(Strings.COMMA)));
        }
        return cb.build();
    }


    public T getFilter() {
        return filter;
    }

    public void setFilter(T filter) {
        this.filter = filter;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Sorting[] getSorting() {
        return sorting;
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