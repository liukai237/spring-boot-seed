package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;


/**
 * 部门管理
 *
 * @author chglee
 * @date 2017-09-27 14:28:36
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_dept")
public class DeptDO extends BaseEntity {

    //上级部门ID，一级部门为0
    private Long parentId;
    //部门名称
    private String name;
    //排序
    private Integer orderNum;
    //是否删除  -1：已删除  0：正常
    private Integer delFlag;
}
