package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.PageData;
import com.iakuil.bf.common.domain.PageRequest;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.dao.entity.Dict;
import com.iakuil.bf.service.DictService;
import com.iakuil.bf.service.converter.DictConverter;
import com.iakuil.bf.service.dto.DictDto;
import com.iakuil.bf.web.vo.DictQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典表接口
 *
 * @author Kai
 */
@Api(value = "DictController", tags = {"数据字典"})
@RequiresRoles("admin")
@RestController
@RequestMapping("/api/dict")
public class DictController extends BaseController {
    private final DictService dictService;
    private final DictConverter dictConverter;

    @Autowired
    public DictController(DictService dictService, DictConverter dictConverter) {
        this.dictService = dictService;
        this.dictConverter = dictConverter;
    }

    @ApiOperation(value = "查询字典类型", notes = "查询所有数据字典类型。")
    @GetMapping(value = "/listType")
    public Resp<List<DictDto>> listType() {
        return ok(dictConverter.toDtoList(dictService.listType()));
    }

    @ApiOperation(value = "字典详情", notes = "根据ID查询数据字典。")
    @GetMapping(value = "/query")
    public Resp<DictDto> queryDetails(@RequestParam Long id) {
        return ok(dictConverter.toDto(dictService.findById(id)));
    }

    @ApiOperation(value = "字典列表（不分页）", notes = "查询所有数据字典数据（不分页）。")
    @PostMapping(value = "/list")
    public Resp<List<DictDto>> queryAll(@RequestBody DictDto dict) {
        return ok(dictConverter.toDtoList(dictService.list(dictConverter.toEntity(dict))));
    }

    @ApiOperation(value = "字典列表", notes = "分页查询数据字典数据。")
    @PostMapping(value = "/listWithPage")
    public Resp<PageData<DictDto>> doQuery(@RequestBody PageRequest<DictQuery> param) {
        return ok(dictService.page(param.as(Dict.class), DictConverter.INSTANCE::toDto));
    }

    @ApiOperation(value = "字典新增", notes = "新增数据字典。")
    @RequiresPermissions("sys:dict:add")
    @PostMapping(value = "/add")
    public Resp<?> doAdd(@RequestBody DictDto dict) {
        return ok(dictService.add(dictConverter.toEntity(dict)));
    }

    @ApiOperation(value = "字典修改", notes = "修改数据字典。")
    @RequiresPermissions("sys:dict:edit")
    @PostMapping(value = "/edit")
    public Resp<?> doChange(@RequestBody DictDto dict) {
        return ok(dictService.modify(dictConverter.toEntity(dict)));
    }

    @ApiOperation(value = "字典删除", notes = "批量删除数据字典。")
    @RequiresPermissions("sys:dict:remove")
    @PostMapping(value = "/remove")
    public Resp<?> doRemove(@RequestParam Long[] ids) {
        return ok(dictService.removeByIds(ids));
    }
}