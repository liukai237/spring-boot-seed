package com.iakuil.seed.domain;

import com.iakuil.seed.constant.Gender;
import com.iakuil.seed.constant.Province;
import com.iakuil.seed.support.DefaultGenId;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class User implements Serializable {

    @Id
    @KeySql(genId = DefaultGenId.class)
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
    private String address;
    @CreatedDate
    private Date createTime;
    @LastModifiedDate
    private Date updateTime;
    //private String salt;
    //private Boolean locked;
    //private Boolean available;
}