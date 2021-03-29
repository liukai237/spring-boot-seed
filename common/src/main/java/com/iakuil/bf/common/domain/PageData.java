package com.iakuil.bf.common.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.github.pagehelper.Page;
import com.iakuil.bf.common.tool.Validate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MyBatis分页结果容器
 *
 * <p>功能同PageInfo，额外支持转换器参数。
 * <p>一般与{@link Pageable}结对出现。
 *
 * @param <T> Entity
 * @author Kai
 */
@Getter
@Setter
@ApiModel(value = "PageData", description = "分页数据")
public class PageData<T> implements Serializable {
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

    /**
     * 额外返回的数据
     * <p>比如：分页同时返回平均年龄。
     * <p>PS. 为避免出现多层嵌套，该属性内字段会被扁平化，与total、pageSize等处于同一层级。
     */
    private Map<String, Object> extra;

    private PageData(long total, List<T> list, int pageNum, int pageSize, int pages, int size) {
        this.total = total;
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pages;
        this.size = size;
    }

    @Deprecated
    public <R> PageData(List<R> data, Function<? super R, ? extends T> mapper) {
        Validate.notEmpty(data, "data list should not be empty!");
        Validate.notNull(mapper, "mapper should not be empty!");

        if (data instanceof Page) {
            Page<T> page = (Page<T>) data;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.size = page.size();
        }

        this.list = data.stream().map(mapper).collect(Collectors.toList());
    }

    public PageData(List<T> list) {
        Validate.notEmpty(list, "Data list should not be empty!");

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

    public <U> PageData<U> map(Function<? super T, ? extends U> converter) {
        Validate.notNull(converter, "Converter should not be empty!");

        return new PageData<>(this.total,
                this.list.stream().map(converter).collect(Collectors.toList()),
                this.pageNum,
                this.pageSize,
                this.pages,
                this.size
        );
    }

    @ApiIgnore
    @JsonAnyGetter
    public Map<String, Object> getExtra() {
        return extra;
    }
}