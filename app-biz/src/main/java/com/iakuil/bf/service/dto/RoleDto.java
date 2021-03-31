package com.iakuil.bf.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ApiModel(value = "RoleDto", description = "角色")
public class RoleDto implements Serializable {
    private Long id;
    private String name;
    private String remark;
    private Date createTime;
}
