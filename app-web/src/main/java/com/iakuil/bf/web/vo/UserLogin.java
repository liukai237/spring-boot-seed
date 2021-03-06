package com.iakuil.bf.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(value = "UserLogin", description = "用户登录参数")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLogin {

    @NotNull(message = "用户名不能为空！")
    @ApiModelProperty(name = "username", value = "用户名，也可以是手机、email或者微信openId。", required = true, example = "13400000000")
    private String username;

    @NotNull(message = "密码不能为空！")
    @ApiModelProperty(name = "password", value = "密码。", required = true, example = "123456")
    private String password;

    @ApiModelProperty(name = "rememberMe", value = "记住我。", example = "false")
    private Boolean rememberMe;
}