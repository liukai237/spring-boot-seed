package com.iakuil.bf.service.dto;

import com.iakuil.bf.common.domain.TreeNode;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * 部门
 *
 * @author Kai
 */
@Getter
@Setter
@ApiModel(value = "DeptDto", description = "部门")
public class DeptDto implements TreeNode<Long> {
    private Long id;
    //上级部门ID，一级部门为0
    private Long parentId;
    //部门名称
    private String name;
    //排序
    private Integer orderNum;

    private List<DeptDto> children;

    @Override
    public Long id() {
        return this.getId();
    }

    @Override
    public Long parentId() {
        return this.parentId;
    }

    @Override
    public boolean root() {
        return Objects.equals(this.parentId, 0L);
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
