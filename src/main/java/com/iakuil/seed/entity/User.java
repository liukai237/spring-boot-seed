package com.iakuil.seed.entity;

import com.iakuil.seed.common.BaseDomain;
import com.iakuil.seed.common.db.SelfGenId;
import com.iakuil.seed.constant.Gender;
import com.iakuil.seed.constant.Province;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class User extends BaseDomain {

    @Id
    @KeySql(genId = SelfGenId.class)
    private Long userId;
    private String username;
    private Gender gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String passwdHash;
    private String tel;
    private String avatar;
    private Province province;
    private String nickname;
    private String email;
    @Column(name = "openid")
    private String openId;
    private String address;
    @CreatedDate
    private Date createTime;
    @LastModifiedDate
    private Date updateTime;
    //private String salt;
    //private Boolean locked;
    //private Boolean available;
}