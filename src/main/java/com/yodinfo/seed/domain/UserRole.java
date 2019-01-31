package com.yodinfo.seed.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole {
    private Long userRoleId;
    private Long userId;
    private Long roleId;
}