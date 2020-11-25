package com.iakuil.seed.dto;

import com.iakuil.seed.common.QueryBase;
import com.iakuil.seed.constant.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UserQueryParam", description = "用户查询参数")
public class UserQueryParam extends QueryBase {

    @ApiModelProperty(name = "uid", value = "用户ID")
    private Long uid;

    @ApiModelProperty(name = "tel", value = "手机号码。", example = "13400000000")
    private String tel;

    @ApiModelProperty(name = "gender", value = "性别", example = "male")
    private Gender gender;
}