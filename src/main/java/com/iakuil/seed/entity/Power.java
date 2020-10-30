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
@Table(name = "t_power")
public class Power extends BaseDomain {
    @Id
    private Long powerId;
    private String powerName;
    private Date createTime;
}