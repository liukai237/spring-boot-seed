package com.iakuil.bf.common;

import com.iakuil.bf.common.constant.RespCode;
import com.iakuil.bf.common.security.UserDetails;
import com.iakuil.bf.common.security.UserDetailsService;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.groups.Default;

/**
 * 视图层基类
 *
 * <p>所有RestController强制继承。
 *
 * <p>关于数据校验：
 * <p>基础数据校验使用JSR 303注解自动完成，通过{@link com.iakuil.bf.common.annotation.ErrorCode}注解可以返回指定错误码；
 * <p>Controller业务逻辑校验结果应该通过`fail()`方法尽早返回；
 * <p>Service层的校验错误直接抛出{@code BusinessException}即可。
 *
 * @author Kai
 */
@Validated(Default.class)
public abstract class BaseController {

    @Resource
    private UserDetailsService userDetailsService;

    public <T> Resp<T> ok() {
        return ok(null);
    }

    public <T> Resp<T> ok(T data) {
        return new Resp<>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), data);
    }

    public <T> Resp<T> ok(String msg) {
        return new Resp<>(RespCode.SUCCESS.getCode(), msg);
    }

    public <T> Resp<T> ok(boolean result) {
        return result ? ok() : fail();
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

    public Long getCurrentUserId() {
        UserDetails user = getCurrentUser();
        return user == null ? null : user.getId();
    }

    public UserDetails getCurrentUser() {
        return userDetailsService.getCurrentUser();
    }
}