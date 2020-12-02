package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Table(name = "t_mp_userinfo")
public class MpUserInfo extends BaseDomain {
    @Id
    @Column(name = "openId")
    private String openId;
    @Column(name = "nickname")
    private String nickName;
    private Integer gender;
    private String city;
    private String province;
    private String country;
    @Column(name = "avatarurl")
    private String avatarUrl;
    @Column(name = "unionid")
    private String unionId;
    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;
    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;
    private Integer source;
}