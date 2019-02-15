package com.yodinfo.seed.domain;

import com.yodinfo.seed.constant.Gender;
import com.yodinfo.seed.constant.Province;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.OrderBy;
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
    private Gender gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String password;
    private String avatar;
    private String phone;
    private Province province;
    private String nickname;
    //private String email;
    //private String address;
    //private String salt;
    //private Boolean locked;
    //private Boolean available;
    private Date createTime;
    private Date updateTime;
}