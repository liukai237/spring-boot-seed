package com.yodinfo.seed.bo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yodinfo.seed.util.BeanMapUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 统一封装的分页请求
 *
 * @param <T>
 */
@ApiModel(value = "Req", description = "统一封装请求")
@Getter
@Setter
public class Req<T> {
    @ApiModelProperty(name = "filter", value = "过滤参数")
    private T filter;

    @ApiModelProperty(value = "分页/排序参数")
    @JsonProperty("pagination")
    @JsonAlias("lazyLoadEvent") // 兼容PrimeNG TurboTable(p-Table)组件
    private Pagination pagination;

    public Map<String, Object> flatAsMap() {
        Map<String, Object> paramMap = BeanMapUtils.beanToMap(filter, true);

        String sortField = this.pagination.sortField;
        if (sortField != null && sortField.length() > 0) {
            paramMap.put("sortField", sortField);
            paramMap.put("sortOrder", this.pagination.getSortOrder() == -1 ? "DESC" : "ASC");
        }

        Integer rows = this.pagination.getSize();
        if (rows != null && rows != 0) {
            paramMap.put("pageSize", rows);

            Integer first = this.pagination.getOffset();
            paramMap.put("pageNum", (first + rows) / rows);
        }

        return paramMap;
    }

    @ApiModel(value = "Pagination", description = "分页及排序")
    @Getter
    @Setter
    private class Pagination {

        @JsonProperty("offset")
        @JsonAlias("first")
        private Integer offset;

        @JsonProperty("size")
        @JsonAlias("rows")
        private Integer size;

        private String sortField;
        private Integer sortOrder;
    }
}