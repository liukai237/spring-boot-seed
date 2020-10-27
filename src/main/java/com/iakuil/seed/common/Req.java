package com.iakuil.seed.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.seed.util.BeanMapUtils;
import com.iakuil.seed.util.Flattenable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.LinkedHashMap;
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
            paramMap.put("pageSize", this.paging.getPageSize());
            paramMap.put("pageNum", this.paging.getPageNum());
        } else {
            paramMap = new LinkedHashMap<>();
            paramMap.put("pageSize", 0); // zero means no paging
            paramMap.put("pageNum", 1);
        }

        if (this.sorting != null) {
            paramMap.put("orderBy", Arrays.stream(sorting)
                    .map(item -> item.getField() + " " + item.getOrder().toString())
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
        @ApiModelProperty(name = "pageNum", value = "分页页码", example = "1")
        @JsonProperty("pageNum")
        private Integer pageNum;
        @ApiModelProperty(name = "pageSize", value = "分页尺寸", example = "10")
        @JsonProperty("pageSize")
        private Integer pageSize;
    }

    /**
     * 排序参数
     */
    @Getter
    @Setter
    @ApiModel(value = "Sorting", description = "排序参数")
    public static class Sorting {
        @ApiModelProperty(name = "field", value = "排序字段")
        private String field;
        @ApiModelProperty(name = "order", value = "排序方向", example = "DESC")
        private Direction order;
    }

    @Getter
    enum Direction {
        ASC, DESC;
    }
}