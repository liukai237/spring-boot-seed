package com.iakuil.bf.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UserLogin", description = "用户登录参数")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLogin {

    @ApiModelProperty(name = "username", value = "用户名，也可以是手机、email或者微信openId。", required = true, example = "13400000000")
    private String username;

    @ApiModelProperty(name = "password", value = "密码。")
    private String password;

    @ApiModelProperty(name = "rememberMe", value = "记住我。", example = "true")
    private Boolean rememberMe;
}