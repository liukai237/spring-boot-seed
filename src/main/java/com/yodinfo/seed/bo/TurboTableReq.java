package com.yodinfo.seed.bo;

import com.yodinfo.seed.util.BeanMapUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * PrimeNG TurboTable(p-Table)组件入参
 * See: https://www.primefaces.org/primeng/#/table
 */
@Getter
@Setter
@ApiModel(value = "TurboTableReq", description = "分页请求")
public class TurboTableReq<T> {
    @ApiModelProperty(value = "查询参数")
    private T param;

    @ApiModelProperty(value = "分页/排序参数")
    private LazyLoadEvent lazyLoadEvent;

    /**
     * 参数 to Map
     */
    public Map<String, Object> flatAsMap() {
        Map<String, Object> paramMap = BeanMapUtils.beanToMap(param, true);

        String sortField = this.lazyLoadEvent.sortField;
        if (sortField != null && sortField.length() > 0) {
            paramMap.put("sortField", sortField);
            paramMap.put("sortOrder", this.lazyLoadEvent.getSortOrder() == -1 ? "DESC" : "ASC");
        }

        Integer rows = this.lazyLoadEvent.getRows();
        if (rows != null && rows != 0) {
            paramMap.put("pageSize", rows);

            Integer first = this.lazyLoadEvent.getFirst();
            paramMap.put("pageNum", (first + rows) / rows);
        }

        return paramMap;
    }

    @Getter
    @Setter
    private class LazyLoadEvent {
        private Integer first;
        private Integer rows;
        private String sortField;
        private Integer sortOrder;
    }
}