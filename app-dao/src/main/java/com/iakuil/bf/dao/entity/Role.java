package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_role")
public class Role extends BaseEntity {
    private String name;
    private String remark;
    private String delFlag;
    @CreatedBy
    private Long userIdCreate;
    @CreatedDate
    private Date createTime;
}