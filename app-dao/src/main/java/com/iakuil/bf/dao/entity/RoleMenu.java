package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Table(name = "t_role_menu")
public class RoleMenu extends BaseEntity {
    private Long roleId;
    private Long menuId;
    private Date createTime;
}