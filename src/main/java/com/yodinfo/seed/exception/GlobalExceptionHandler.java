package com.yodinfo.seed.exception;

import com.yodinfo.seed.common.Resp;
import com.yodinfo.seed.constant.RespCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.Field;
import java.sql.SQLException;
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
        log.error("[SERVER ERROR] {}", e.getMessage());
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
    public Resp<?> processMethodArgumentNotValidException(MethodArgumentNotValidException e) throws NoSuchFieldException {
        log.error("[PARAM ERROR]", e);
        Integer code = null;
        String msg = null;
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            String fieldName = fieldError.getField();
            Class<?> parameterType = e.getParameter().getParameterType();
            Field field = parameterType.getDeclaredField(fieldName);
            ErrorCode annotation = field.getAnnotation(ErrorCode.class);
            if (annotation != null && annotation.value() != -1) {
                code = annotation.value();
                msg = annotation.message();
            }
        }

        return new Resp<>(code == null ? RespCode.BAD_REQUEST.getCode() : code, StringUtils.defaultIfBlank(msg, e.getBindingResult()
                .getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining())));
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processSqlException(SQLException e) {
        log.error("[DB ERROR]", e);
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), DEFAULT_ERROR_MESSAGE);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processDataAccessExceptionException(DataAccessException e) {
        log.error("[DB ERROR]", e);
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), DEFAULT_ERROR_MESSAGE);
    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processCannotGetJdbcConnectionException(CannotGetJdbcConnectionException e) {
        log.error("[DB ERROR]\n{}", e.getMessage());
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), "网络繁忙，请稍后再试！");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processOtherException(Exception e) {
        log.error("[UNKNOWN ERROR]", e);
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), "Occurring an unknown exception! Please check with admin if you want to get the details!", e.getMessage());
    }
}