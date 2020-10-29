package com.iakuil.seed.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.iakuil.seed.constant.Gender;
import com.iakuil.seed.support.LabelEnumDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UserQueryParam", description = "用户查询参数")
public class UserQueryParam {

    @ApiModelProperty(name = "uid", value = "用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    @ApiModelProperty(name = "tel", value = "手机号码。", example = "13400000000")
    private String tel;

    @JsonDeserialize(using = LabelEnumDeserializer.class)
    @ApiModelProperty(name = "gender", value = "性别", example = "male")
    private Gender gender;
}