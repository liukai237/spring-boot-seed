package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.BaseDomain;
import com.iakuil.bf.common.constant.Gender;
import com.iakuil.bf.common.constant.Province;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.LogicDelete;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class User extends BaseDomain {

    @Id
    private Long id;
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
    @Column(updatable = false)
    private Date createTime;
    @LastModifiedDate
    private Date updateTime;
    @Version
    private Integer version;
    @LogicDelete
    private Integer deleted;
    //private String salt;
    //private Boolean locked;
    //private Boolean available;
}