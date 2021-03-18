package com.iakuil.bf.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iakuil.bf.common.constant.Gender;
import com.iakuil.bf.common.constant.Province;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Past;
import java.util.Date;

@Getter
@Setter
@ApiModel(value = "ProfileEdit", description = "修改用户信息参数")
public class ProfileEdit {

    @ApiModelProperty(name = "gender", value = "性别", example = "male")
    private Gender gender;

    @Past
    @ApiModelProperty(name = "birthday", value = "生日", example = "1999-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty(name = "nickname", value = "用户昵称", example = "Harry")
    private String nickname;

    private String avatar;

    @ApiModelProperty(name = "province", value = "省份")
    private Province province;

    @ApiModelProperty(name = "address", value = "详细地址")
    private String address;
}