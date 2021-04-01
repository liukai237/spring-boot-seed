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

    @ApiOperation(value = "获取部门列表", notes = "获取部门列表")
    @RequiresPermissions("sys:dept:list")
    @GetMapping("/list")
    @ResponseBody()
    Resp<List<DeptDto>> list() {
        return ok(deptService.list(new DeptDO(), DeptConverter.INSTANCE::toDto));
    }

    @ApiOperation(value = "获取部门列表树", notes = "获取部门列表树")
    @RequiresPermissions("sys:dept:list")
    @GetMapping("/listAsTree")
    @ResponseBody()
    Resp<List<DeptDto>> listAsTree() {
        return ok(TreeUtils.generateTrees(BeanUtils.copyMany(deptService.findAll(), DeptDto.class)));
    }

    @RequiresPermissions("sys:dept:add")
    @PostMapping("/save")
    Resp<?> add(@RequestBody DeptDto dept) {
        return ok(deptService.add(DeptConverter.INSTANCE.toEntity(dept)));
    }

    @RequiresPermissions("sys:dept:edit")
    @PostMapping("/edit")
    Resp<?> edit(@RequestBody DeptDto dept) {
        return ok(deptService.modifyWithVersion(DeptConverter.INSTANCE.toEntity(dept)));
    }

    @RequiresPermissions("sys:dept:remove")
    @PostMapping("/remove")
    Resp<?> remove(@RequestParam Long id) {
        DeptDO query = new DeptDO();
        query.setParentId(id);
        int count = deptService.count(query);
        if (count > 0) {
            return fail("包含下级部门,不允许修改");
        }

        //TODO 部门包含用户,不允许修改
        return ok(deptService.removeById(id));
    }
}
