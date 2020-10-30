package com.iakuil.seed.entity;

import com.iakuil.seed.common.BaseDomain;
import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_role")
public class Role extends BaseDomain {
    @Id
    private Long roleId;
    private String roleName;
    private Date createTime;
}