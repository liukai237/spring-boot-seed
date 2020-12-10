package com.iakuil.bf.service.dto;


import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;


@ApiModel(value = "DictDto", description = "数据字典")
public class DictDto {

    //编号
    private Long id;
    //标签名
    private String name;
    //数据值
    private String value;
    //类型
    private String type;
    //描述
    private String description;
    //排序（升序）
    private BigDecimal sort;
    //父级编号
    private Long parentId;
    //备注信息
    private String remark;

    /**
     * 设置：编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：标签名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：标签名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：数据值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取：数据值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置：类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取：类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置：描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取：描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置：排序（升序）
     */
    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    /**
     * 获取：排序（升序）
     */
    public BigDecimal getSort() {
        return sort;
    }

    /**
     * 设置：父级编号
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取：父级编号
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置：备注信息
     */
    public void setRemarks(String remark) {
        this.remark = remark;
    }

    /**
     * 获取：备注信息
     */
    public String getRemark() {
        return remark;
    }
}
