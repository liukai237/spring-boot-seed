package com.iakuil.bf.service.dto;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@ApiModel(value = "DictDto", description = "数据字典")
public class DictDto {

    /**
     * 编号
     */
    private Long id;
    /**
     * 标签名
     */
    private String name;
    /**
     * 数据值
     */
    private String value;
    /**
     * 类型
     */
    private String type;
    /**
     * 描述
     */
    private String description;
    /**
     * 排序（升序）
     */
    private BigDecimal sort;
    /**
     * 父级编号
     */
    private Long parentId;
    /**
     * 备注信息
     */
    private String remark;
}
