package com.yodinfo.seed.domain;

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
@Table(name = "t_user_role")
public class UserRole implements Serializable {
    private Long userId;
    @Id
    private Long roleId;
    private Date createTime;
}