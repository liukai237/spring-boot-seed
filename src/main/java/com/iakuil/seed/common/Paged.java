package com.iakuil.seed.common;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MyBatis分页结果容器
 * <p>功能同PageInfo，额外支持转换器参数</p><br/>
 * <p>序列化时属性会被扁平化，不会多出一层嵌套。</p>
 *
 * @param <T> Entity
 */
@Getter
@Setter
@ApiModel(value = "Paged", description = "分页数据")
public class Paged<T> implements Serializable {
    @ApiModelProperty(value = "总记录数")
    private long total;
    @ApiModelProperty(value = "结果集")
    private List<T> data;
    @ApiModelProperty(value = "第几页")
    private int pageNum;
    @ApiModelProperty(value = "每页记录数")
    private int pageSize;
    @ApiModelProperty(value = "总页数")
    private int pages;
    @ApiModelProperty(value = "当前页的数量")
    private int size;
    @ApiModelProperty(value = "附加数据", notes = "额外返回的数据，比如：分页同时返回平均年龄。")
    private Map<String, Object> extra;

    public Paged(List<T> data) {
        Objects.requireNonNull(data, "data list should not be empty!");

        if (data instanceof Page) {
            Page<T> page = (Page<T>) data;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.size = page.size();
        }

        this.data = data;
    }

    public <R> Paged(List<R> data, Function<? super R, ? extends T> mapper) {
        Objects.requireNonNull(data, "data list should not be empty!");

        if (data instanceof Page) {
            Page<T> page = (Page<T>) data;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.size = page.size();
        }

        this.data = data.stream().map(mapper).collect(Collectors.toList());
    }
}