package com.iakuil.bf.web.aop;

import com.iakuil.bf.common.annotation.ErrorCode;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.common.enums.RespCode;
import com.iakuil.bf.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
 *
 * @author Kai
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

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("[PARAM ERROR]", e);
        // e.g. Required String parameter 'xxx' is not present
        return new Resp<>(RespCode.BAD_REQUEST.getCode(), e.getMessage());
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
        log.error("[SQL ERROR]", e);
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), DEFAULT_ERROR_MESSAGE);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processDuplicateKeyException(DuplicateKeyException e) {
        log.error("[DB ERROR]", e);
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), "数据库中已存在该记录！");
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
        log.error("[DB ERROR]{}", e.getMessage());
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), "网络繁忙，请稍后再试！");
    }

    @ExceptionHandler({LockedAccountException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processLockedAccountException(LockedAccountException e) {
        log.error("[ACCOUNT LOCKED]{}", e.getMessage());
        return new Resp<>(RespCode.UNAUTHORIZED.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = {CredentialsException.class, UnknownAccountException.class,})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processCredentialsException(AuthenticationException e) {
        log.error("[PWD ERROR]{}", e.getMessage());
        return new Resp<>(RespCode.UNAUTHORIZED.getCode(), "用户名或密码错误！");
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processAuthorizationException(AuthorizationException e) {
        log.error("[AUTH ERROR]{}", e.getMessage());
        return new Resp<>(RespCode.FORBIDDEN.getCode(), "无权限！");
    }

    @ExceptionHandler(ShiroException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processShiroException(ShiroException e) {
        log.error("[AUTH ERROR]{}", e.getMessage());
        return new Resp<>(RespCode.UNAUTHORIZED.getCode(), "未登录！");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processOtherException(Exception e) {
        log.error("[UNKNOWN ERROR]", e);
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), "Occurring an unknown exception! Please check with admin if you want to get the details!", e.getMessage());
    }
}