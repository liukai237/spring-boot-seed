package com.yodinfo.seed.exception;

import org.springframework.core.NestedExceptionUtils;

public class BusinessException extends RuntimeException {
    protected static final String DEFAULT_ERROR_MESSAGE = "Occurring an unknown exception!";

    public BusinessException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), this.getCause());
    }

    public Throwable getRootCause() {
        return NestedExceptionUtils.getRootCause(this);
    }
}