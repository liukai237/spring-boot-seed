package com.iakuil.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 *
 * <p>参考HTTP状态码的语义，可自行扩展</p>
 */
@Getter
@AllArgsConstructor
public enum RespCode {
    FAIL(-1, "FAIL"), // 操作失败。约定：如果错误码为-1，则前端直接显示错误消息。
    SUCCESS(100000, "SUCCESS"), // 操作成功
    NORESULT(100204, "No Records."), // 查询无结果
    BAD_REQUEST(100400, "Bad Request."), // Bad Request
    UNAUTHORIZED(100401, "Unauthorized"), // 未认证（签名错误）
    FORBIDDEN(100403, "No Permission!"), // 无权限
    NOT_FOUND(100404, "Resource Not Found."), // 资源不存在
    INTERNAL_SERVER_ERROR(100500, "A server error has occurred."), // 未知错误、服务器内部错误
    INVALID_TOKEN(200001, "Invalid Token!"),
    INVALID_CAPTCHA(200002, "Invalid Captcha!"),
    INVALID_SMS_CODE(200002, "Invalid SMS Code!");

    private final int code;
    private final String message;

    @Override
    public String toString() {
        return code + ": " + message;
    }
}