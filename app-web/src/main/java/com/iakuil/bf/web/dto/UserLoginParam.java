package com.iakuil.bf.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "LoginParam", description = "用户登录参数")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginParam {

    @ApiModelProperty(name = "username", value = "用户名，也可以是手机、email或者微信openId。", required = true, example = "13400000000")
    private String username;

    @ApiModelProperty(name = "password", value = "密码。")
    private String password;

    @ApiModelProperty(name = "rememberMe", value = "记住我。", example = "true")
    private Boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}