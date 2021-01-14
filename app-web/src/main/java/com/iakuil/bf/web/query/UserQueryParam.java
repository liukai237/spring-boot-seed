package com.iakuil.bf.web.query;

import com.iakuil.bf.common.constant.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UserQueryParam", description = "用户查询参数")
public class UserQueryParam {

    @ApiModelProperty(name = "userId", value = "用户ID")
    private Long id;

    @ApiModelProperty(name = "tel", value = "手机号码。", example = "13400000000")
    private String tel;

    @ApiModelProperty(name = "gender", value = "性别", example = "1")
    private Gender gender;
}