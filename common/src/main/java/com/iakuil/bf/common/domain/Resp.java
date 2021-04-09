package com.iakuil.bf.common.domain;

import com.iakuil.bf.common.enums.RespCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 通用API响应体
 *
 * <p>统一封装Controller层返回结果。
 * <p>code需要定义在{@link RespCode}中，其中success为冗余字段。
 *
 * @author Kai
 */
@ApiModel(value = "Resp", description = "统一API响应结果")
public class Resp<T> implements Serializable {
    public Resp() {
    }

    @ApiModelProperty(value = "状态码")
    private int code;
    @ApiModelProperty(value = "描述信息")
    private String msg;
    @ApiModelProperty(value = "响应数据")
    private T data;

    public Resp(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public Resp(int code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public Resp(RespCode rc) {
        this.code = rc.getCode();
        this.msg = rc.getMessage();
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}