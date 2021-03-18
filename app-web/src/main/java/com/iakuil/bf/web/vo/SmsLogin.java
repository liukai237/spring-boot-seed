package com.iakuil.bf.web.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ApiModel(value = "SmsLogin", description = "手机验证码参数")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsLogin {

    @ApiModelProperty(name = "tel", value = "电话号码，一般为手机号码。", example = "13400000000", required = true)
    @Pattern(regexp = "^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$", message = "无效的手机格式！")
    @JsonProperty("tel")
    @JsonAlias({"mobile", "account"})
    private String tel;

    @ApiModelProperty(name = "smsCode", value = "手机验证码", example = "123456", required = true)
    @JsonProperty("smsCode")
    @Size(max = 6, min = 4, message = "验证码长度必须大于4小于6位！")
    private String smsCode;
}