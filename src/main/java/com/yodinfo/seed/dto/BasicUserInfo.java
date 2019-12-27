package com.yodinfo.seed.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yodinfo.seed.constant.Gender;
import com.yodinfo.seed.constant.Province;
import com.yodinfo.seed.util.CodeEnumDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
@ApiModel(value = "BasicUserInfo", description = "用户基本信息")
public class BasicUserInfo {

    @NotNull(message = "用户ID不能为空！")
    @ApiModelProperty(name = "uid", value = "用户ID")
    private String uid;

    @ApiModelProperty(name = "tel", value = "手机号码。", example = "13400000000")
    @Pattern(regexp = "^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$", message = "无效的手机格式！")
    private String phone;

    @JsonDeserialize(using = CodeEnumDeserializer.class)
    @ApiModelProperty(name = "gender", value = "性别", example = "male")
    private Gender gender;

    @Past
    @ApiModelProperty(name = "birthday", value = "生日", example = "1999-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @Email(message = "电子邮件格式错误！")
    @ApiModelProperty(name = "email", value = "电子邮件", example = "guest@yodinfo.com")
    private String email;

    @ApiModelProperty(name = "nickname", value = "用户昵称", example = "tom2000")
    private String nickname;

    private String avatar;

    @ApiModelProperty(name = "province", value = "省份")
    private Province province;

    @ApiModelProperty(name = "address", value = "详细地址")
    private String address;

    @ApiModelProperty(name = "regTime", value = "注册时间", readOnly = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // 似乎和swagger有冲突
    private Date regTime;
}