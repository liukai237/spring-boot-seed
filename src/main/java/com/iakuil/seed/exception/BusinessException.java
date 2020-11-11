package com.iakuil.seed.exception;

import com.iakuil.seed.constant.RespCode;
import org.springframework.core.NestedExceptionUtils;

/**
 * 业务异常
 *
 * <p>自定义异常，用于封装业务层所有Checked Exception。
 * 可传递错误码，会被{@link GlobalExceptionHandler}捕获。</p>
 */
public class BusinessException extends RuntimeException {
    private int code = RespCode.FAIL.getCode();

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code) {
        super();
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BusinessException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(RespCode rc) {
        this(rc.getMessage(), rc.getCode());
    }

    @Override
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), this.getCause());
    }

    public Throwable getRootCause() {
        return NestedExceptionUtils.getRootCause(this);
    }

    public int getCode() {
        return code;
    }
}