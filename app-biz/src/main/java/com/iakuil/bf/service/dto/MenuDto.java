package com.iakuil.bf.service.dto;

import com.iakuil.bf.common.domain.TreeNode;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ApiModel(value = "MenuDto", description = "目录")
public class MenuDto implements TreeNode<Long> {
    private Long id;
    private Long parentId;
    private String name;
    private String url;
    // 授权(多个用逗号分隔，如：user:list,user:create)
    private String perms;
    // 类型 0：目录 1：菜单 2：按钮
    private Integer type;
    // 菜单图标
    private String icon;
    // 排序
    private Integer orderNum;

    private List<MenuDto> children;

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
