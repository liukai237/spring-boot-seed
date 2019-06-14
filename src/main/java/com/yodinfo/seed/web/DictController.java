package com.yodinfo.seed.web;

import com.yodinfo.seed.bo.PageData;
import com.yodinfo.seed.bo.Req;
import com.yodinfo.seed.bo.Resp;
import com.yodinfo.seed.domain.Dict;
import com.yodinfo.seed.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @PostMapping("/list")
    public Resp<PageData<Dict>> list(@RequestBody Req req) {
        return ok(dictService.listWithPaging(req));
    }
}