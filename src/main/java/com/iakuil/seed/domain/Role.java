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
@Table(name = "t_role")
public class Role implements Serializable {
    @Id
    private Long roleId;
    private String roleName;
    private Date createTime;
}