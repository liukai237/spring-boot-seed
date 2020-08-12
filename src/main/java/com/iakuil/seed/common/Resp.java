package com.iakuil.seed.common;

import com.iakuil.seed.constant.RespCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 统一API响应结果封装
 *
 * <p>用于封装Controller层返回结果，配合{@link RespCode}使用。
 * 其中success为冗余字段。</p>
 */
@Getter
@Setter
@ApiModel(value = "Resp", description = "统一API响应结果")
public class Resp<T> {
    @ApiModelProperty(value = "状态码")
    private int code;
    @ApiModelProperty(value = "描述信息")
    private String message;
    @ApiModelProperty(value = "响应数据")
    private T data;

    public Resp(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Resp(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return code == RespCode.SUCCESS.getCode();
    }
}