package com.iakuil.bf.dao.entity;

import com.iakuil.bf.common.domain.BaseEntity;
import com.iakuil.bf.common.enums.Gender;
import com.iakuil.bf.common.enums.Province;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.LogicDelete;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class User extends BaseEntity {

    @Column(unique = true)
    private String username;
    private Gender gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String passwdHash;
    @Column(unique = true)
    private String tel;
    private String avatar;
    private Province province;
    private String nickname;
    @Column(unique = true)
    private String email;
    private String address;
    private Boolean available;
    @CreatedDate
    @Column(updatable = false)
    private Date createTime;
    @LastModifiedDate
    private Date updateTime;
    @Version
    private Long version;
    @LogicDelete
    private Integer deleted;
}