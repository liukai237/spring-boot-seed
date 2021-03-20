package com.iakuil.bf.web.vo;

import com.iakuil.bf.common.annotation.Phone;
import com.iakuil.bf.common.enums.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UserQuery", description = "用户查询参数")
public class UserQuery {

    @ApiModelProperty(name = "userId", value = "用户ID")
    private Long id;

    @ApiModelProperty(name = "tel", value = "手机号码。", example = "13400000000")
    @Phone
    private String tel;

    @ApiModelProperty(name = "gender", value = "性别", example = "1")
    private Gender gender;
}