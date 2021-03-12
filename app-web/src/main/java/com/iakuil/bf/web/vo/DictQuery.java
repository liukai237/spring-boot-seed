package com.iakuil.bf.web.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "DictQuery", description = "数据字典查询参数")
public class DictQuery {
    @ApiModelProperty(name = "id", value = "编号")
    private Long id;
    @ApiModelProperty(name = "name", value = "标签名")
    private String name;
    @ApiModelProperty(name = "value", value = "数据值")
    private String value;
    @ApiModelProperty(name = "type", value = "类型")
    private String type;
    @ApiModelProperty(name = "description", value = "描述")
    private String description;
    @ApiModelProperty(name = "parentId", value = "父级编号")
    private Long parentId;
}