package com.iakuil.bf.common;

import com.iakuil.bf.common.constant.RespCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 通用API响应体
 *
 * <p>统一封装Controller层返回结果。
 * <p>code需要定义在{@link RespCode}中，其中success为冗余字段。
 *
 * @author Kai
 */
@ApiModel(value = "Resp", description = "统一API响应结果")
public class Resp<T> {
    public Resp() {
    }

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

    public Resp(RespCode rc) {
        this.code = rc.getCode();
        this.message = rc.getMessage();
    }

    public boolean isSuccess() {
        return code == RespCode.SUCCESS.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}