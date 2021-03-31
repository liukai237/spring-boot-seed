package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.common.tool.TreeUtils;
import com.iakuil.bf.dao.entity.MenuDO;
import com.iakuil.bf.service.MenuService;
import com.iakuil.bf.service.converter.MenuConverter;
import com.iakuil.bf.service.dto.MenuDto;
import com.iakuil.bf.web.security.PermissionUtils;
import com.iakuil.toolkit.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单接口
 *
 * @author Kai
 */
@Api(value = "MenuController", tags = {"菜单接口"})
@Slf4j
@RestController
@RequestMapping("/api/menu/")
public class MenuController extends BaseController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @RequiresPermissions("sys:menu:list")
    @GetMapping("/list")
    Resp<List<MenuDto>> list() {
        return ok(menuService.list(new MenuDO(), MenuConverter.INSTANCE::toDto));
    }

    @ApiOperation(value = "获取目录树", notes = "普通用户进入主页或者管理员角色修改界面获取目录树。")
    @RequiresPermissions("sys:dept:list")
    @GetMapping("/tree")
    Resp<List<MenuDto>> listAsTree(@RequestParam Long roleId) {
        List<MenuDO> menus = menuService.findByRoleId(roleId);
        return ok(TreeUtils.generateTrees(BeanUtils.copyMany(menus, MenuDto.class)));
    }

    @RequiresPermissions("sys:menu:add")
    @PostMapping("/save")
    Resp<?> add(@RequestBody MenuDto menu) {
        return ok(menuService.add(MenuConverter.INSTANCE.toEntity(menu)));
    }

    @RequiresPermissions("sys:menu:edit")
    @PostMapping("/edit")
    Resp<?> edit(@RequestBody MenuDto menu) {
        return ok(menuService.modifyWithVersion(MenuConverter.INSTANCE.toEntity(menu)));
    }

    @RequiresPermissions("sys:menu:remove")
    @PostMapping("/remove")
    Resp<?> remove(@RequestParam Long id) {
        return ok(menuService.removeById(id));
    }

    @ApiOperation(value = "获取所有权限标识", notes = "获取系统中所有权限标识。")
    @GetMapping("perms")
    public Resp<List<String>> getAllPerms() {
        return ok(PermissionUtils.getAllPerms());
    }
}