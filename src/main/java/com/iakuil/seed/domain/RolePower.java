package com.iakuil.seed.domain;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_role_power")
public class RolePower implements Serializable {
    private Long roleId;
    @Id
    private Long powerId;
    private Date createTime;
}