package com.yodinfo.seed.exception;

import com.yodinfo.seed.common.Resp;
import com.yodinfo.seed.constant.RespCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "Occurring an server exception!";

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processBusinessException(BusinessException e) {
        log.error("[SERVER ERROR]", e);
        String message = e.getMessage();
        Throwable rootCause = e.getRootCause();
        String finalMsg = message == null ? (rootCause == null ? DEFAULT_ERROR_MESSAGE : rootCause.getMessage()) : message;
        return new Resp<>(e.getCode(), finalMsg);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processBindException(BindException e) {
        log.error("[PARAM ERROR]", e);
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return new Resp<>(RespCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Resp<?> processConstraintViolationException(ConstraintViolationException e) {
        log.error("[PARAM ERROR]", e);
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return new Resp<>(RespCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Resp<?> processMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[PARAM ERROR]", e);
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return new Resp<>(RespCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processDuplicateKeyExceptionException(DuplicateKeyException e) {
        Throwable rootCause = e.getRootCause();
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), "duplicate key!", rootCause == null ? null : rootCause.getMessage());
    }

    @ExceptionHandler(NestedRuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processNestedRuntimeException(NestedRuntimeException e) {
        log.error("[SERVER ERROR]", e);
        Throwable rootCause = e.getRootCause();
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), rootCause == null ? e.getMessage() : rootCause.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processOtherException(Exception e) {
        log.error("[UNKNOWN ERROR]", e);
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), "Occurring an unknown exception! Please check with admin if you want to get the details!", e.getMessage());
    }
}