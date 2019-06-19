package com.yodinfo.seed.exception;

public class JsonBodyNotValidException extends RuntimeException {
    private String report;

    public JsonBodyNotValidException(String message, String report) {
        super(message);
        this.report = report;
    }

    public String getReport() {
        return report;
    }
}