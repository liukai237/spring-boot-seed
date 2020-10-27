package com.iakuil.seed.web;

import com.iakuil.seed.common.BaseController;
import com.iakuil.seed.domain.Dict;
import com.iakuil.seed.service.DictService;
import com.iakuil.seed.common.Paged;
import com.iakuil.seed.common.Req;
import com.iakuil.seed.common.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 字典表接口
 * 演示原生SpringMVC + 原生MyBatis XML
 */
@Controller
@RequestMapping("/api/dict")
public class DictController extends BaseController {
    private final DictService dictService;

    @Autowired
    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @PostMapping("/list")
    public Resp<Paged<Dict>> list(@RequestBody Req<Dict> req) {
        return ok(dictService.listWithPage(req.flatAsMap()));
    }
}