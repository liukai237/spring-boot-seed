package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.Resp;
import com.iakuil.bf.common.db.PageData;
import com.iakuil.bf.dao.entity.Dict;
import com.iakuil.bf.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典表接口
 * <p>演示原生SpringMVC + 原生MyBatis XML</p>
 */
@RestController
@RequestMapping("/api/dict")
public class DictController extends BaseController {
    private final DictService dictService;

    @Autowired
    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<PageData<Dict>> list(@RequestBody Dict dict) {
        return ok(dictService.listWithPage(dict));
    }
}