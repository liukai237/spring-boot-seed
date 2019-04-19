package com.yodinfo.seed.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yodinfo.seed.util.BeanMapUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

@ApiModel(value = "Req", description = "分页请求")
@Getter
@Setter
@NoArgsConstructor
public class Req<T> {
    @ApiModelProperty(name = "param", value = "过滤参数")
    private T param;


    public Req(T param, Pagination pagination, PageSort pageSort) {
        Objects.requireNonNull(param, "param should not be empty!");
        this.param = param;
    }

    @JsonIgnore
    public Map<String, Object> flatAsMap() {
        return BeanMapUtils.beanToMap(param, true);
    }
}