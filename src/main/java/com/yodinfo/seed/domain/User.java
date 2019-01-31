package com.yodinfo.seed.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    private Long userId;
    private String username;
    private Integer gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String password;
    private String avatar;
    private String phone;
    private Integer province;
    //private String email;
    //private String address;
    //private String salt;
    //private Boolean locked;
    //private Boolean available;
    private Date createTime;
    private Date updateTime;
}