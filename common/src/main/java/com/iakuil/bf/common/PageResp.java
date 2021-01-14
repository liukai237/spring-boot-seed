package com.iakuil.bf.common;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页/排序请求响应结果
 * <p>一般与{@link PageQuery}结对出现。</p>
 *
 * @param <T> Entity
 * @author Kai
 */
@Getter
@Setter
@ApiModel(value = "PageResp", description = "分页数据")
public class PageResp<T> extends Resp<List<T>> {
    @ApiModelProperty(value = "总记录数")
    private long total;
    @ApiModelProperty(value = "第几页")
    private int pageNum;
    @ApiModelProperty(value = "每页记录数")
    private int pageSize;
    @ApiModelProperty(value = "总页数")
    private int pages;
    @ApiModelProperty(value = "当前页的数量")
    private int size;

    public PageResp(List<T> list) {
        Objects.requireNonNull(list, "data list should not be empty!");

        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.size = page.size();
        }

        super.setData(list);
    }

    public <R> PageResp(List<R> data, Function<? super R, ? extends T> mapper) {
        Objects.requireNonNull(data, "data list should not be empty!");

        if (data instanceof Page) {
            Page<T> page = (Page<T>) data;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.size = page.size();
        }

        this.setData(data.stream().map(mapper).collect(Collectors.toList()));
    }
}