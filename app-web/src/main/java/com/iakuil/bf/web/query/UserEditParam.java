package com.iakuil.bf.web.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iakuil.bf.common.constant.Gender;
import com.iakuil.bf.common.constant.Province;
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
@ApiModel(value = "UserEditParam", description = "修改用户信息参数")
public class UserEditParam {

    @NotNull(message = "用户ID不能为空！")
    @ApiModelProperty(name = "id", value = "用户ID")
    private Long id;

    @ApiModelProperty(name = "tel", value = "手机号码。", example = "13400000000")
    @Pattern(regexp = "^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$", message = "无效的手机格式！")
    private String tel;

    @ApiModelProperty(name = "gender", value = "性别", example = "male")
    private Gender gender;

    @Past
    @ApiModelProperty(name = "birthday", value = "生日", example = "1999-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @Email(message = "电子邮件格式错误！")
    @ApiModelProperty(name = "email", value = "电子邮件", example = "guest@163.com")
    private String email;

    @ApiModelProperty(name = "nickname", value = "用户昵称", example = "Harry")
    private String nickname;

    private String avatar;

    @ApiModelProperty(name = "province", value = "省份")
    private Province province;

    @ApiModelProperty(name = "address", value = "详细地址")
    private String address;
}