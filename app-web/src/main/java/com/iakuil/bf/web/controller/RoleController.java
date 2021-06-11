package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.dao.entity.Role;
import com.iakuil.bf.service.RoleService;
import com.iakuil.bf.service.converter.RoleConverter;
import com.iakuil.bf.service.dto.RoleDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色接口
 *
 * @author Kai
 */
@Api(value = "RoleController", tags = {"角色接口"})
@Slf4j
@Controller
@RequestMapping("/api/role")
public class RoleController extends BaseController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "角色列表", notes = "获取所有角色。")
    @RequiresPermissions("sys:role:list")
    @GetMapping("/list")
    @ResponseBody()
    Resp<List<RoleDto>> doQuery() {
        return ok(roleService.list(new Role(), RoleConverter.INSTANCE::toDto));
    }

    @ApiOperation(value = "角色新增", notes = "增加角色。")
    @RequiresPermissions("sys:role:add")
    @PostMapping("/add")
    Resp<?> doAdd(@RequestBody RoleDto role) {
        return ok(roleService.add(RoleConverter.INSTANCE.toEntity(role)));
    }

    @ApiOperation(value = "角色修改", notes = "修改角色。")
    @RequiresPermissions("sys:role:edit")
    @PostMapping("/edit")
    Resp<?> doChange(@RequestBody RoleDto role) {
        return ok(roleService.modifyWithVersion(RoleConverter.INSTANCE.toEntity(role)));
    }

    @ApiOperation(value = "角色删除", notes = "删除角色。")
    @RequiresPermissions("sys:role:remove")
    @PostMapping("/remove")
    Resp<?> doRemove(@RequestParam Long id) {
        return ok(roleService.removeById(id));
    }
}
