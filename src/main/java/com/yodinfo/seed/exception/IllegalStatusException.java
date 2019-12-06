package com.yodinfo.seed.exception;

public class IllegalStatusException extends RuntimeException {
    private Integer code;

    public IllegalStatusException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}