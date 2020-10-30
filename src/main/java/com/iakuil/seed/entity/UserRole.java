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
@Table(name = "t_user_role")
public class UserRole extends BaseDomain {
    private Long userId;
    @Id
    private Long roleId;
    private Date createTime;
}