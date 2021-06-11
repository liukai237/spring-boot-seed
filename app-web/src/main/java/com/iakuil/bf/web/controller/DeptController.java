package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.common.tool.TreeUtils;
import com.iakuil.bf.dao.entity.DeptDO;
import com.iakuil.bf.service.DeptService;
import com.iakuil.bf.service.converter.DeptConverter;
import com.iakuil.bf.service.dto.DeptDto;
import com.iakuil.toolkit.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门接口
 *
 * @author Kai
 */
@Api(value = "DeptController", tags = {"部门接口"})
@Slf4j
@Controller
@RequestMapping("/api/dept/")
public class DeptController extends BaseController {

    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @ApiOperation(value = "部门列表", notes = "获取部门列表。")
    @RequiresPermissions("sys:dept:list")
    @GetMapping("/list")
    Resp<List<DeptDto>> doQuery() {
        return ok(deptService.list(new DeptDO(), DeptConverter.INSTANCE::toDto));
    }

    @ApiOperation(value = "部门新增", notes = "新增部门。")
    @RequiresPermissions("sys:dept:add")
    @PostMapping("/save")
    Resp<?> doAdd(@RequestBody DeptDto dept) {
        return ok(deptService.add(DeptConverter.INSTANCE.toEntity(dept)));
    }

    @ApiOperation(value = "部门编辑", notes = "编辑部门。")
    @RequiresPermissions("sys:dept:edit")
    @PostMapping("/edit")
    Resp<?> doChange(@RequestBody DeptDto dept) {
        return ok(deptService.modifyWithVersion(DeptConverter.INSTANCE.toEntity(dept)));
    }

    @ApiOperation(value = "部门删除", notes = "删除部门。")
    @RequiresPermissions("sys:dept:remove")
    @PostMapping("/remove")
    Resp<?> doRemove(@RequestParam Long id) {
        DeptDO query = new DeptDO();
        query.setParentId(id);
        if (deptService.count(query) > 0) {
            return fail("包含下级部门,不允许删除！");
        }

        return ok(deptService.removeById(id));
    }

    @ApiOperation(value = "部门树", notes = "获取部门列表树。")
    @RequiresPermissions("sys:dept:list")
    @GetMapping("/listAsTree")
    Resp<List<DeptDto>> listAsTree() {
        return ok(TreeUtils.generateTrees(BeanUtils.copyMany(deptService.findAll(), DeptDto.class)));
    }
}
