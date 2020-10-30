package com.iakuil.seed.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.seed.common.Flattenable;
import com.iakuil.seed.common.tool.BeanMapUtils;
import com.iakuil.seed.common.tool.Strings;
import com.iakuil.seed.constant.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统一封装的分页请求
 *
 * <p>建议使用Java Bean封装查询参数。</p>
 *
 * @param <T> 过滤条件
 */
@ApiModel(value = "Req", description = "统一封装的请求，分页和排序参数分开封装")
@Getter
@Setter
public class Req<T> implements Flattenable {

    @ApiModelProperty(name = "filtering", value = "过滤参数。")
    private T filter;

    @ApiModelProperty(value = "paging", notes = "分页参数。")
    private Paging paging;

    @ApiModelProperty(value = "sorting", notes = "排序参数。")
    private Sorting[] sorting;

    @Override
    public Map<String, Object> flatAsMap() {
        Map<String, Object> paramMap = BeanMapUtils.beanToMap(getFilter(), true);
        if (this.paging != null) {
            paramMap.put(SysConstant.DEFAULT_PAGE_SIZE_FIELD, this.paging.getPageSize());
            paramMap.put(SysConstant.DEFAULT_PAGE_NUM_FIELD, this.paging.getPageNum());
        } else {
            paramMap.put(SysConstant.DEFAULT_PAGE_SIZE_FIELD, 0); // zero means no paging
            paramMap.put(SysConstant.DEFAULT_PAGE_NUM_FIELD, 1);
        }

        if (this.sorting != null) {
            paramMap.put(SysConstant.DEFAULT_ORDER_FIELD, Arrays.stream(sorting)
                    .map(item -> Strings.toUnderlineCase(item.getField()) + " " + item.getOrder().toString())
                    .collect(Collectors.joining()));
        }

        return paramMap;
    }

    /**
     * 分页参数
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