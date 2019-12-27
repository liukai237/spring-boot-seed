package com.yodinfo.seed.web;

import com.yodinfo.seed.bo.Resp;
import com.yodinfo.seed.constant.RespCode;

abstract class BaseController {

    Resp<?> ok() {
        return ok(null);
    }

    <T> Resp<T> ok(T data) {
        return new Resp<>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), data);
    }

    Resp<?> fail() {
        return new Resp<>(RespCode.FAIL.getCode(), RespCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    public Resp<?> fail(int code) {
        return new Resp<>(code, RespCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    Resp<?> fail(String msg) {
        return new Resp<>(RespCode.FAIL.getCode(), msg);
    }

    Resp<?> fail(RespCode respCode) {
        return new Resp<>(respCode.getCode(), respCode.getMessage());
    }
}