package com.iakuil.bf.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户认证信息（用于缓存）
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
    private String password;
    private Set<String> roles;
    private Set<String> permissions;
}