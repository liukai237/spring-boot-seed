package com.yodinfo.seed.common;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yodinfo.seed.util.BeanMapUtils;
import com.yodinfo.seed.util.Flattenable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;

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

    @ApiModelProperty(name = "filter", value = "过滤参数")
    private T filter;

    @ApiModelProperty(value = "分页参数")
    private Paging paging;

    @Override
    public Map<String, Object> flatAsMap() {
        Map<String, Object> paramMap = ObjectUtils.defaultIfNull(BeanMapUtils.beanToMap(getFilter(), true), new LinkedHashMap<>());
        if (this.paging != null) {
            paramMap.put("pageSize", this.paging.getPageSize());
            paramMap.put("pageNum", this.paging.getPageNum());
            paramMap.put("orderBy", this.paging.getOrderBy());
        }

        return paramMap;
    }

    /**
     * 分页参数
     * 同时支持pageNum/pageSize和offset/count两种风格
     * 其中pageSize/count为必需条件，否则默认为0。
     */
    @Getter
    @Setter
    public class Paging {
        @ApiModelProperty(name = "pageNum", value = "分页页码", example = "1")
        @JsonProperty("pageNum")
        @JsonAlias({"page", "pageIndex", "pageNo"})
        private Integer pageNum;
        @ApiModelProperty(name = "pageSize", value = "分页尺寸", example = "10")
        @JsonProperty("pageSize")
        @JsonAlias({"size", "count", "rows", "results", "limit"})
        private Integer pageSize;
        @ApiModelProperty(name = "offset", value = "位移量，从第几条记录起", example = "0")
        @JsonProperty("offset")
        @JsonAlias("first")
        private Integer offset;
        @ApiModelProperty(name = "orderBy", value = "排序参数")
        @JsonProperty("orderBy")
        @JsonAlias("sort")
        private String orderBy;

        public Integer getPageNum() {
            if (!gtZero(pageSize)) {
                return 0;
            }

            if (gtZero(pageNum)) {
                return pageNum;
            }

            if (gtZero(offset)) {
                return (offset + pageSize) / pageSize;
            }

            return pageNum;
        }

        public Integer getOffset() {
            if (!gtZero(pageSize)) {
                return 0;
            }

            if (gtZero(offset)) {
                return offset;
            }

            if (gtZero(pageNum)) {
                return (pageNum - 1) * pageSize;
            }

            return offset;
        }

        private boolean gtZero(Integer num) {
            return num != null && num > 0;
        }
    }
}