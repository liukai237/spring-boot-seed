package com.iakuil.seed.web;

import com.iakuil.seed.constant.RespCode;
import com.iakuil.seed.common.Resp;

abstract class BaseController {

    public <T> Resp<T> ok() {
        return ok(null);
    }

    public <T> Resp<T> ok(T data) {
        return new Resp<>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), data);
    }

    public <T> Resp<T> fail() {
        return new Resp<>(RespCode.FAIL.getCode(), RespCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    public <T> Resp<T> fail(int code) {
        return new Resp<>(code, RespCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    public <T> Resp<T> fail(String msg) {
        return new Resp<>(RespCode.FAIL.getCode(), msg);
    }

    public <T> Resp<T> fail(RespCode respCode) {
        return new Resp<>(respCode.getCode(), respCode.getMessage());
    }
}