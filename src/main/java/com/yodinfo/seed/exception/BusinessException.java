package com.yodinfo.seed.exception;

import com.yodinfo.seed.constant.RespCode;
import org.springframework.core.NestedExceptionUtils;

public class BusinessException extends RuntimeException {
    private static final String DEFAULT_ERROR_MESSAGE = "Occurring an server exception!";
    private int code = RespCode.INTERNAL_SERVER_ERROR.getCode();

    public BusinessException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code) {
        super(DEFAULT_ERROR_MESSAGE);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(int code, Throwable cause) {
        super(DEFAULT_ERROR_MESSAGE, cause);
        this.code = code;
    }

    public BusinessException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

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