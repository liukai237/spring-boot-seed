package com.yodinfo.seed.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yodinfo.seed.util.Jsv;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ApiModel(value = "UserRegInfo", description = "用户注册信息")
@Jsv("user_reg")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegInfo {

    /**
     * 三大运营商号码均可验证(不含卫星通信1349)
     * <br>　　　　　2018年3月已知
     * 中国电信号段
     * 133,149,153,173,177,180,181,189,199
     * 中国联通号段
     * 130,131,132,145,155,156,166,175,176,185,186
     * 中国移动号段
     * 134(0-8),135,136,137,138,139,147,150,151,152,157,158,159,178,182,183,184,187,188,198
     * 其他号段
     * 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
     * 虚拟运营商
     * 电信：1700,1701,1702
     * 移动：1703,1705,1706
     * 联通：1704,1707,1708,1709,171
     * 卫星通信：148(移动) 1349
     */
    @ApiModelProperty(name = "tel", value = "电话号码，一般为手机号码。", example = "13400000000", required = true)
    @Pattern(regexp = "^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$", message = "无效的手机格式！")
    @JsonProperty("tel")
    @JsonAlias({"mobile", "account"})
    private String tel;

    @NotEmpty(message = "密码不能为空！")
    @Size(max = 24, min = 6, message = "密码长度必须大于6小于24位！")
    @ApiModelProperty(name = "password", value = "用户密码。", example = "123456", required = true)
    @JsonProperty("password")
    @JsonAlias({"Password", "passWd", "passwd"})
    private String password;

    @ApiModelProperty(name = "vcode", value = "手机验证码", example = "123456", required = true)
    @JsonProperty("vcode")
    @Size(max = 6, min = 4, message = "验证码长度必须大于4小于6位！")
    private String vCode;
}