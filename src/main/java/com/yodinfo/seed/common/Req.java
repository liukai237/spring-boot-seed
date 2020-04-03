package com.yodinfo.seed.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yodinfo.seed.util.BeanMapUtils;
import com.yodinfo.seed.util.Flattenable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统一封装的分页请求
 *
 * @param <T>
 */
@ApiModel(value = "Req", description = "统一封装的请求，分页和排序参数分开封装")
@Getter
@Setter
public class Req<T> implements Flattenable {

    @ApiModelProperty(name = "filter", value = "过滤参数。")
    private T filter;

    @ApiModelProperty(value = "paging", notes = "包含排序参数。")
    private Paging paging;

    @ApiModelProperty(value = "sorting", notes = "排序参数。")
    private Sorting sorting;

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
            paramMap.put("sortField", this.sorting.getField());
            paramMap.put("sortOrder", this.sorting.getOrder());
        }

        return paramMap;
    }

    /**
     * 分页参数
     */
    @Getter
    @Setter
    public static class Paging {
        @ApiModelProperty(name = "pageNum", value = "分页页码", example = "1")
        @JsonProperty("pageNum")
        private Integer pageNum;
        @ApiModelProperty(name = "pageSize", value = "分页尺寸", example = "10")
        @JsonProperty("pageSize")
        private Integer pageSize;
    }

    @Getter
    @Setter
    public static class Sorting {
        private String field;
        private String order;
    }
}