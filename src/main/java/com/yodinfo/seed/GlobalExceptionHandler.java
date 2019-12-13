package com.yodinfo.seed;

import com.yodinfo.seed.bo.Resp;
import com.yodinfo.seed.config.RespCode;
import com.yodinfo.seed.exception.BusinessException;
import com.yodinfo.seed.exception.IllegalStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processBusinessException(BusinessException e) {
        log.error("[SERVER ERROR]", e);
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), e.getRootCause().getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(e.getMessage());
        return new Resp<>(400, errorMsg, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        e.printStackTrace();
        String errorMsg = e.getName() + " is missing!"; //TODO...
        return new Resp<>(400, errorMsg, null);
    }

    @ExceptionHandler(IllegalStatusException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processIllegalStatusException(IllegalStatusException e) {
        log.error("[STATUS ERROR]", e);
        return new Resp<>(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> DuplicateKeyExceptionException(DuplicateKeyException e) {
        Throwable rootCause = e.getRootCause();
        if (rootCause != null && rootCause.getMessage().contains("username")) {
            log.error(rootCause.getMessage());
            return new Resp<>(400, "duplicate mobile!", null);
        }
        return new Resp<>(400, "duplicate key!", e.getRootCause());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processAuthenticationException(AuthenticationException e) {
        log.error("[AUTH ERROR]", e.getMessage());
        return new Resp<>(RespCode.UNAUTHORIZED.getCode(), RespCode.UNAUTHORIZED.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Resp<?> processOtherException(Exception e) {
        log.error("[UNKNOWN ERROR]", e.getMessage());
        return new Resp<>(RespCode.INTERNAL_SERVER_ERROR.getCode(), RespCode.INTERNAL_SERVER_ERROR.getMessage(), null);
    }
}