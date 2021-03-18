package com.iakuil.bf.web;

import com.google.common.collect.Sets;
import com.iakuil.bf.common.security.UserDetails;
import com.iakuil.bf.common.security.UserDetailsService;
import com.iakuil.bf.dao.entity.Power;
import com.iakuil.bf.dao.entity.Role;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.PowerService;
import com.iakuil.bf.service.RoleService;
import com.iakuil.bf.service.UserService;
import com.iakuil.toolkit.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户详情服务（认证授权用）
 *
 * @author Kai
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final RoleService roleService;
    private final PowerService powerService;

    public UserDetailsServiceImpl(UserService userService, RoleService roleService, PowerService powerService) {
        this.userService = userService;
        this.roleService = roleService;
        this.powerService = powerService;
    }

    /**
     * 从安全上下文获取UserDetails
     */
    @Override
    public UserDetails getCurrentUser() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        return principal == null ? null : (UserDetails) principal;
    }

    /**
     * 根据ID、用户名、电话号码、邮箱等唯一标识获取用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String identity) {
        User user = userService.findByIdentity(identity);

        if (user != null) {
            UserDetails details = BeanUtils.copy(user, UserDetails.class);
            Long id = user.getId();
            details.setRoles(roleService.findRolesByUserId(id).stream().map(Role::getRoleName).collect(Collectors.toSet()));
            details.setPermissions(findPowersByUserId(id).stream().map(Power::getPowerName).collect(Collectors.toSet()));
            return details;
        }

        return null;
    }

    private Set<Power> findPowersByUserId(Long userId) {
        Set<Role> roles = roleService.findRolesByUserId(userId);
        Set<Power> powers = Sets.newHashSet();
        for (Role role : roles) {
            powers.addAll(powerService.findPowersByRoleId(role.getId()));
        }

        return powers;
    }
}
