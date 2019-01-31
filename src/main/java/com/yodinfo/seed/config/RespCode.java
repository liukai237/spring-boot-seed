package com.yodinfo.seed.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
@Getter
@AllArgsConstructor
public enum RespCode {
    SUCCESS(200, "SUCCESS"), // 成功
    NORESULT(204, "No records."), // 查询无结果
    FAIL(400, "Bad Request."), // Bad Request
    UNAUTHORIZED(401, "Unauthorized"), // 未认证（签名错误）
    FORBIDDEN(403, "Account has been owed!"), // 接口调用次数为0
    NOT_FOUND(404, "Invalid API."), // 接口不存在
    INTERNAL_SERVER_ERROR(500, "A server error has occurred."); // 服务器内部错误

    private final int code;
    private final String message;

    @Override
    public String toString() {
        return code + ": " + message;
    }
}