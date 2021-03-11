package com.iakuil.bf.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iakuil.bf.common.constant.Gender;
import com.iakuil.bf.common.constant.Province;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户登录后返回的基本信息
 *
 * @author Kai
 */
@Getter
@Setter
public class BasicInfoDto {
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
    private String sessionId;
    private String address;
    private Date createTime;
}
