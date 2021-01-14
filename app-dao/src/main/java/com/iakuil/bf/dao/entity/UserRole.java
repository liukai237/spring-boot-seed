package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.BaseEntity;
import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user_role")
public class UserRole extends BaseEntity {
    @Id
    private Long id;
    private Long userId;
    private Long roleId;
    private Date createTime;
}