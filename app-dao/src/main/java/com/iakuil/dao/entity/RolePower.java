package com.iakuil.dao.entity;

import com.iakuil.common.BaseDomain;
import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_role_power")
public class RolePower extends BaseDomain {
    private Long roleId;
    @Id
    private Long powerId;
    private Date createTime;
}