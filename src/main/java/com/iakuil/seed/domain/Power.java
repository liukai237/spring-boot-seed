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
@Table(name = "t_power")
public class Power implements Serializable {
    @Id
    private Long powerId;
    private String powerName;
    private Date createTime;
}