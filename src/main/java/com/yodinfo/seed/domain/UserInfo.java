package com.yodinfo.seed.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_userinfo")
public class UserInfo implements Serializable {
    private Integer subscribe; // 0 means no
    @Id
    @Column(name = "openid")
    private String openId;
    @Column(name = "nickname")
    private String nickName;
    private Integer sex;
    private String language;
    private String city;
    private String province;
    private String country;
    @Column(name = "headimgurl")
    private String headImgUrl;
    @Column(name = "subscribe_time")
    private Date subscribeTime;
    @Column(name = "unionid")
    private String unionId;
    private String remark;
    @Column(name = "groupid")
    private Integer groupId;
    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;
    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;
}