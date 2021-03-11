package com.iakuil.bf.service;

import com.google.common.collect.Sets;
import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.common.UserDetails;
import com.iakuil.bf.dao.*;
import com.iakuil.bf.dao.entity.*;
import com.iakuil.toolkit.BeanUtils;
import com.iakuil.toolkit.HashIdUtils;
import com.iakuil.toolkit.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends BaseService<User> {

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
        // 使用HashIds生成唯一username
        String username = user.getUsername();
        user.setUsername(StringUtils.defaultString(username, HashIdUtils.encrypt(Long.parseLong(user.getTel()))));

        String pwdHash = null;
        try {
            pwdHash = PasswordHash.createHash(user.getPasswdHash());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage()); // should not be happened
        }
        log.debug("Encrypted passwd: {}", pwdHash);
        user.setPasswdHash(pwdHash);
        return super.add(user);
    }

    @Transactional(readOnly = true)
    public Set<Power> findPowersByUserId(Long userId) {
        Set<Role> roles = this.findRolesByUserId(userId);
        Set<Power> powers = Sets.newHashSet();
        for (Role role : roles) {
            powers.addAll(findPowersByRole(role.getId()));
        }

        return powers;
    }

    @Transactional(readOnly = true)
    public Set<Power> findPowersByRole(Long roleId) {
        RolePower condition = new RolePower();
        condition.setRoleId(roleId);
        List<RolePower> rolePowers = rolePowerMapper.select(condition);

        Set<Power> powers = Sets.newHashSet();
        for (RolePower rolePower : rolePowers) {
            Power condition2 = new Power();
            condition2.setId(rolePower.getPowerId());
            powers.addAll(powerMapper.select(condition2));
        }

        return powers;
    }

    @Transactional(readOnly = true)
    public Set<Role> findRolesByUserId(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        Set<Long> roleIds = null;
        if (user != null) {
            UserRole condition = new UserRole();
            condition.setUserId(userId);
            List<UserRole> userRoles = userRoleMapper.select(condition);
            roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        }

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