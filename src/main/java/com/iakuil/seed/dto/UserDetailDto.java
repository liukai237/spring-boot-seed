package com.iakuil.seed.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.iakuil.seed.constant.Gender;
import com.iakuil.seed.constant.Province;
import com.iakuil.seed.support.LabelEnumDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel(value = "UserDetailDto", description = "用户基本信息（脱敏）")
public class UserDetailDto {

    @ApiModelProperty(name = "uid", value = "用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    @ApiModelProperty(name = "tel", value = "手机号码。", example = "13400000000")
    private String tel;

    @JsonDeserialize(using = LabelEnumDeserializer.class)
    @ApiModelProperty(name = "gender", value = "性别", example = "male")
    private Gender gender;

    @ApiModelProperty(name = "birthday", value = "生日", example = "1999-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty(name = "email", value = "电子邮件", example = "guest@163.com")
    private String email;

    @ApiModelProperty(name = "nickname", value = "用户昵称", example = "tom2000")
    private String nickname;

    private String avatar;

    @ApiModelProperty(name = "province", value = "省份")
    private Province province;

    @ApiModelProperty(name = "address", value = "详细地址")
    private String address;

    @ApiModelProperty(name = "regTime", value = "注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date regTime;
}