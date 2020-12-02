package com.iakuil.web.query;

import com.iakuil.common.PageQuery;
import com.iakuil.common.constant.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UserQueryParam", description = "用户查询参数")
public class UserPageQueryParam extends PageQuery {

    @ApiModelProperty(name = "userId", value = "用户ID")
    private Long userId;

    @ApiModelProperty(name = "tel", value = "手机号码。", example = "13400000000")
    private String tel;

    @ApiModelProperty(name = "gender", value = "性别", example = "male")
    private Gender gender;
}