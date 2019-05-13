package com.yodinfo.seed.bo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yodinfo.seed.util.BeanMapUtils;
import com.yodinfo.seed.util.Flattenable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 特殊格式的分页请求
 * 此处仅为举例，当需要特殊格式的封装时，应实现Flattenable接口，AOP会自动实现分页。
 * @param <T>
 */
@ApiModel(value = "Req2", description = "分页排序参数封装在一起的请求")
@Getter
@Setter
public class Req2<T> extends AbstractReq<T> implements Flattenable {

    @JsonProperty("pagination")
    @JsonAlias("lazyLoadEvent") // 兼容PrimeNG TurboTable(p-Table)组件
    private Pagination pagination;

    @Override
    public Map<String, Object> flatAsMap() {
        Map<String, Object> paramMap = BeanMapUtils.beanToMap(getFilter(), true);

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

        @ApiModelProperty(name = "offset", value = "位移量，从第几条记录起", example = "0")
        @JsonProperty("offset")
        @JsonAlias("first")
        private Integer offset;

        @ApiModelProperty(name = "size", value = "分页尺寸", example = "10")
        @JsonProperty("size")
        @JsonAlias("rows")
        private Integer size;

        private String sortField;
        private Integer sortOrder;
    }
}