package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_power")
public class Power extends BaseEntity {
    @Id
    private Long id;
    private String powerName;
    private Date createTime;
}