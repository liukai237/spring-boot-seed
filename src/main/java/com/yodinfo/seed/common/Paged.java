package com.yodinfo.seed.common;

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
 * 分页结果容器
 * PageHelper的PageInfo简化版本
 *
 * @param <T>
 */
@Getter
@Setter
@ApiModel(value = "Paged", description = "分页数据")
public class Paged<T> implements Serializable {
    @ApiModelProperty(value = "总记录数")
    private long total;
    @ApiModelProperty(value = "结果集")
    private List<T> list;
    @ApiModelProperty(value = "第几页")
    private int pageNum;
    @ApiModelProperty(value = "每页记录数")
    private int pageSize;
    @ApiModelProperty(value = "总页数")
    private int pages;
    @ApiModelProperty(value = "当前页的数量")
    private int size;
    @ApiModelProperty(value = "附加数据", notes = "全局附加数据，字段不确定") // e.g. 分页同时返回平均年龄
    private Map<String, Object> extra;

    public Paged(List<T> list) {
        Objects.requireNonNull(list, "data list should not be empty!");

        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.size = page.size();
        }

        this.list = list;
    }

    public <R> Paged(List<R> list, Function<? super R, ? extends T> mapper) {
        Objects.requireNonNull(list, "data list should not be empty!");

        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.size = page.size();
        }

        this.list = list.stream().map(mapper).collect(Collectors.toList());
    }
}