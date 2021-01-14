package com.iakuil.bf.common;

import com.iakuil.bf.common.constant.RespCode;

/**
 * 视图层基类
 * <p>所有RestController强制继承。</p>
 *
 * @author Kai
 */
public abstract class BaseController {

    public <T> Resp<T> ok() {
        return ok(null);
    }

    public <T> Resp<T> ok(T data) {
        return new Resp<>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), data);
    }

    public <T> Resp<T> ok(PageResp<T> data) {
        data.setCode(RespCode.SUCCESS.getCode());
        data.setMessage(RespCode.SUCCESS.getMessage());
        return (Resp<T>) data;
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

    public <T> Resp<T> done(boolean result) {
        return result ? ok() : fail();
    }
}