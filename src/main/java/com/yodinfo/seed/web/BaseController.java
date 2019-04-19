package com.yodinfo.seed.web;

import com.yodinfo.seed.bo.Resp;
import com.yodinfo.seed.config.RespCode;

abstract class BaseController {

    Resp<?> ok() {
        return new Resp<>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), null);
    }

    <T> Resp<T> ok(T data) {
        return new Resp<>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), data);
    }

    Resp<?> fail() {
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), RespCode.INTERNAL_SERVER_ERROR.getMessage(), null);
    }

    Resp<?> fail(String msg) {
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), msg, null);
    }
}