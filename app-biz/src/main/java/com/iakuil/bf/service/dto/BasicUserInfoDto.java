package com.iakuil.bf.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iakuil.bf.common.constant.Gender;
import com.iakuil.bf.common.constant.Province;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BasicUserInfoDto {
    private Long id;
    private String username;
    private Gender gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String tel;
    private String avatar;
    private Province province;
    private String nickname;
    private String email;
    private String openId;
    private String sessionId;
    private String address;
    private Date createTime;
}
