package com.iakuil.bf.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户认证信息
 *
 * <p>用于安全框架缓存，必须有一个{@code getId()}方法。
 *
 * @author Kai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails implements Serializable {
    private Long id;
    private String username;
    private String passwdHash;
    private String tel;
    private String email;

    private Set<String> roles;
    private Set<String> permissions;
}