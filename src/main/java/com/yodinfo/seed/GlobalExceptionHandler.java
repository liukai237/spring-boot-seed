package com.yodinfo.seed;

import com.yodinfo.seed.bo.Resp;
import com.yodinfo.seed.exception.BusinessException;
import com.yodinfo.seed.exception.JsonBodyNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Resp<?> processBusinessException(BusinessException e) {
        LOGGER.error("[SERVER ERROR]", e);
        return new Resp<>(500, e.getRootCause().getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Resp<?> processMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(e.getMessage());
        return new Resp<>(400, errorMsg, null);
    }

    @ExceptionHandler(JsonBodyNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Resp<?> processJsonBodyNotValidException(JsonBodyNotValidException e) {
        LOGGER.error("[JSV ERROR]", e);
        return new Resp<>(400, e.getMessage(), e.getReport());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Resp<?> DuplicateKeyExceptionException(DuplicateKeyException e) {
        Throwable rootCause = e.getRootCause();
        if (rootCause != null && rootCause.getMessage().contains("username")) {
            LOGGER.error(rootCause.getMessage());
            return new Resp<>(400, "duplicate mobile!", null);
        }
        return new Resp<>(400, "duplicate key!", e.getRootCause());
    }

//    @ExceptionHandler(ShiroException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ResponseBody
//    public Resp<String> handleShiroException(ShiroException e) {
//        return new Resp<>(500, "A auth error has occurred.", e.getMessage());
//    }
}