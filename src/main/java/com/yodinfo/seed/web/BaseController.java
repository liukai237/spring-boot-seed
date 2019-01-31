package com.yodinfo.seed.web;

import com.yodinfo.seed.bo.Resp;
import com.yodinfo.seed.config.RespCode;

abstract class BaseController {

    <T> Resp<T> ok(T data) {
        return new Resp<>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), data);
    }
}