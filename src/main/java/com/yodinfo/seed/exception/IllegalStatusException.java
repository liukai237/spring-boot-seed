package com.yodinfo.seed.exception;

public class IllegalStatusException extends RuntimeException {
    private Integer code = -1;

    public IllegalStatusException(String message) {
        super(message);
    }

    public IllegalStatusException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}