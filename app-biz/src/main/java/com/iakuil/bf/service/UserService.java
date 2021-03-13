package com.iakuil.bf.service;

import com.google.common.collect.Sets;
import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.common.UserDetails;
import com.iakuil.bf.dao.*;
import com.iakuil.bf.dao.entity.*;
import com.iakuil.toolkit.BeanUtils;
import com.iakuil.toolkit.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户服务
 *
 * @author Kai
 */
@Slf4j
@Service
public class UserService extends BaseService<User> {
    private static final Hashids HASHIDS = new Hashids("Just4ShortId", 8, "abcdefghijklmnopqrstuvwxyz1234567890");

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PowerMapper powerMapper;
    private final RolePowerMapper rolePowerMapper;
    private final UserRoleMapper userRoleMapper;

    public UserService(UserMapper userMapper, RoleMapper roleMapper, PowerMapper powerMapper, RolePowerMapper rolePowerMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.powerMapper = powerMapper;
        this.rolePowerMapper = rolePowerMapper;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(User user) {
        preAdd(user);
        return super.add(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addAll(List<User> entities) {
        entities.forEach(this::preAdd);
        return userMapper.insertList(entities) > entities.size();
    }

    private void preAdd(User entity) {
        // 如果username为空，则生成wxid开头的临时用户名
        entity.setUsername(StringUtils.defaultString(entity.getUsername(), "wxid_" + HASHIDS.encode(Long.parseLong(entity.getTel()))));
        entity.setPasswdHash(PasswordHash.createHash(entity.getPasswdHash()));
        entity.setVersion(1L);
    }

    @Transactional(readOnly = true)
    public Set<Power> findPowersByUserId(Long userId) {
        Set<Role> roles = this.findRolesByUserId(userId);
        Set<Power> powers = Sets.newHashSet();
        for (Role role : roles) {
            powers.addAll(findPowersByRoleId(role.getId()));
        }

        return powers;
    }

    @Transactional(readOnly = true)
    public Set<Power> findPowersByRoleId(Long roleId) {
        RolePower condition = new RolePower();
        condition.setRoleId(roleId);
        List<RolePower> rolePowers = rolePowerMapper.select(condition);

        if (CollectionUtils.isNotEmpty(rolePowers)) {
            return new HashSet<>(powerMapper.selectByIds(rolePowers.stream().map(String::valueOf).collect(Collectors.joining(","))));
        }
        return Collections.emptySet();
    }

    @Transactional(readOnly = true)
    public Set<Role> findRolesByUserId(Long userId) {
        UserRole condition = new UserRole();
        condition.setUserId(userId);
        Set<Long> roleIds = userRoleMapper.select(condition).stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(roleIds)) {
            return new HashSet<>(roleMapper.selectByIds(roleIds.stream().map(String::valueOf).collect(Collectors.joining(","))));
        }
        return Collections.emptySet();
    }

    /**
     * 根据多种ID获取用户信息（认证用）
     */
    public UserDetails findUserDetails(String identity) {
        User user = userMapper.selectByIdentity(identity);

        if (user != null) {
            UserDetails details = BeanUtils.copy(user, UserDetails.class);
            details.setPassword(user.getPasswdHash());

            Long id = user.getId();
            details.setRoles(findRolesByUserId(id).stream().map(Role::getRoleName).collect(Collectors.toSet()));
            details.setPermissions(findPowersByUserId(id).stream().map(Power::getPowerName).collect(Collectors.toSet()));
            return details;
        }

        return null;
    }
}