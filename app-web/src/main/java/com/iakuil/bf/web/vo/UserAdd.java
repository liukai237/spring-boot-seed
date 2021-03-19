package com.iakuil.bf.web.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.bf.common.annotation.Password;
import com.iakuil.bf.common.annotation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ApiModel(value = "UserAdd", description = "用户注册参数")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAdd {
    @ApiModelProperty(name = "tel", value = "电话号码，一般为手机号码。", example = "13400000000", required = true)
    @Phone
    @JsonProperty("tel")
    @JsonAlias({"mobile", "account"})
    private String tel;

    @Password
    @ApiModelProperty(name = "password", value = "用户密码。", example = "Changeme_123", required = true)
    @JsonProperty("password")
    private String password;

    @ApiModelProperty(name = "smsCode", value = "手机验证码", example = "123456", required = true)
    @JsonProperty("smsCode")
    @Size(max = 6, min = 4, message = "验证码长度必须大于4小于6位！")
    private String smsCode;
}