package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.BaseDomain;
import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_role")
public class Role extends BaseDomain {
    @Id
    private Long id;
    private String roleName;
    private Date createTime;
}