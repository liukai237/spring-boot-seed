package com.yodinfo.seed.web;

import com.yodinfo.seed.common.Paged;
import com.yodinfo.seed.common.Req;
import com.yodinfo.seed.common.Resp;
import com.yodinfo.seed.domain.Dict;
import com.yodinfo.seed.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 字典表
 */
@Controller
@RequestMapping("/common/dict")
public class DictController extends BaseController {
    private final DictService dictService;

    @Autowired
    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @PostMapping("/list")
    public Resp<Paged<Dict>> list(@RequestBody Req<Dict> req) {
        return ok(dictService.listWithPaging(req.flatAsMap()));
    }
}