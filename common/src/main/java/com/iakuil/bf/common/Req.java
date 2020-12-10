package com.iakuil.bf.common;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.bf.common.constant.CommonConstant;
import com.iakuil.bf.common.tool.BeanUtils;
import com.iakuil.bf.common.tool.Strings;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 复杂查询请求体
 *
 * <p>【强制】所有分页接口必须使用该组件作为唯一的入参（自动完成这种简单的接口除外）。</p><br/>
 * <p>【建议】使用Java Bean而不是Map作为filtering查询参数。</p><br/>
 * <p>除filtering、paging和sorting之外的属性统一放入other，不直接参与数据库查询逻辑。
 * 比如：某接口需要在业务层对比签名，又不想将它作为过滤条件，这时就可以单独传入该参数。
 * 请不要滥用这种半公开的传参方式！</p>
 *
 * @param <T> 过滤条件
 */
@ApiModel(value = "Req", description = "统一封装的请求体")
public class Req<T extends PageQuery> {

    @ApiModelProperty(name = "filtering", value = "过滤参数。")
    private T filter;

    @ApiModelProperty(value = "paging", notes = "分页参数。")
    private Paging paging;

    @ApiModelProperty(value = "sorting", notes = "排序参数。")
    private Sorting[] sorting;

    @ApiModelProperty(hidden = true)
    private Map<String, Object> other = new LinkedHashMap<>();

    @JsonAnySetter
    public void setOther(String key, Object value) {
        this.other.put(key, value);
    }

    @JsonIgnore
    public PageQuery getQuery() {
        PageQuery query = BeanUtils.copy(this.filter, filter.getClass());
        if (this.paging != null) {
            query.setPageSize(this.paging.getPageSize());
            query.setPageNum(this.paging.getPageNum());
        } else {
            query.setPageSize(0); // zero means no paging
            query.setPageNum(1);
        }

        if (this.sorting != null) {
            query.setOrderBy(Arrays.stream(sorting)
                    .filter(item -> StringUtils.isNoneBlank(item.getField()))
                    .map(item -> Strings.toUnderlineCase(item.getField()) + " " + item.getOrder().toString())
                    .collect(Collectors.joining()));
        }

        return query;
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

    public Map<String, Object> getOther() {
        return other;
    }

    public void setOther(Map<String, Object> other) {
        this.other = other;
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