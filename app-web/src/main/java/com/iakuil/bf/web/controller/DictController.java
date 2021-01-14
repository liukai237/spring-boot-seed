package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.PageQuery;
import com.iakuil.bf.common.Resp;
import com.iakuil.bf.service.DictService;
import com.iakuil.bf.service.converter.DictConverter;
import com.iakuil.bf.service.dto.DictDto;
import com.iakuil.bf.web.query.DictQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典表接口
 * <p>演示原生SpringMVC + 原生MyBatis XML</p><br/>
 * <p>适用于某些开源或者项目的模块移植。</p>
 */
@Api(value = "DictController", tags = {"数据字典"})
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
    @GetMapping(value = "/listType", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<List<DictDto>> listType() {
        return ok(dictConverter.toDtoList(dictService.listType()));
    }

    @ApiOperation(value = "查询单个数据字典", notes = "根据ID查询数据字典。")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<DictDto> list(@RequestParam Long id) {
        return ok(dictConverter.toDto(dictService.get(id)));
    }

    @ApiOperation(value = "查询数据字典（不分页）", notes = "查询所有数据字典数据（不分页）。")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<List<DictDto>> list(@RequestBody DictDto dict) {
        return ok(dictConverter.toDtoList(dictService.list(dictConverter.toEntity(dict))));
    }

    @ApiOperation(value = "分页查询数据字典", notes = "分页查询数据字典数据。")
    @PostMapping(value = "/listWithPage", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<DictDto> listWithPage(@RequestBody PageQuery<DictQueryParam> param) {
        return ok(dictService.listWithPage(param));
    }

    @ApiOperation(value = "新增数据字典", notes = "新增数据字典。")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> add(@RequestBody DictDto dict) {
        return done(dictService.save(dictConverter.toEntity(dict)) > 0);
    }

    @ApiOperation(value = "修改数据字典", notes = "修改数据字典。")
    @PostMapping(value = "/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> modify(@RequestBody DictDto dict) {
        return done(dictService.update(dictConverter.toEntity(dict)) > 0);
    }

    @ApiOperation(value = "删除数据字典", notes = "批量删除数据字典。")
    @PostMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> remove(@RequestParam String[] ids) {
        return done(dictService.batchRemove(ids) > 0);
    }
}