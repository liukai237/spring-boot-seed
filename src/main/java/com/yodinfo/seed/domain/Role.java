package com.yodinfo.seed.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Role {
    private Long roleId;
    private String roleName;
    private String description;
    private Integer order;
    private Date createTime;
}