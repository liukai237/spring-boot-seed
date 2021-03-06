package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Deprecated
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_role_power")
public class RolePower extends BaseEntity {
    @Id
    private Long id;
    private Long roleId;
    private Long powerId;
    private Date createTime;
}