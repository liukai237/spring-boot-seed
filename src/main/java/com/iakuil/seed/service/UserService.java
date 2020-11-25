package com.iakuil.seed.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.iakuil.seed.annotation.StartPage;
import com.iakuil.seed.common.BaseService;
import com.iakuil.seed.common.Paged;
import com.iakuil.seed.common.QueryBase;
import com.iakuil.seed.converter.UserConverter;
import com.iakuil.seed.dao.*;
import com.iakuil.seed.dto.UserDetailDto;
import com.iakuil.seed.entity.*;
import com.iakuil.seed.util.HashIdUtils;
import com.iakuil.seed.util.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends BaseService {

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

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userMapper.selectOne(User.builder().userId(userId).build());
    }

    @StartPage
    @Transactional(readOnly = true)
    public Paged<UserDetailDto> findWithPage(Integer pageNum, Integer pageSize, String orderBy) {
        return new Paged<>(userMapper.selectAll(), UserConverter.INSTANCE::toDto);
    }

    @StartPage(orderBy = "create_time desc")
    @Transactional(readOnly = true)
    public Paged<UserDetailDto> findByCondition(Map<String, Object> condition) {
        return new Paged<>(userMapper.selectByCondition(null), UserConverter.INSTANCE::toDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean add(User user) {
        String username = user.getUsername();
        user.setUsername(StringUtils.defaultString(username, HashIdUtils.encrypt(System.currentTimeMillis()))); // 使用HashIds生成唯一username

        String pwdHash = null;
        try {
            pwdHash = PasswordHash.createHash(user.getPasswdHash());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage()); // should not be happened
        }
        log.debug("Encrypted passwd: {}", pwdHash);
        user.setPasswdHash(pwdHash);
        return userMapper.insertSelective(user) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean modify(User user) {
        User entity = userMapper.selectByPrimaryKey(user.getUserId());
        user.setVersion(entity.getVersion());
        return userMapper.updateByPrimaryKeySelective(user) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long uid) {
        userMapper.deleteByPrimaryKey(uid);
        log.info("users {} were deleted!", uid);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String... ids) {
        int count = userMapper.deleteByIds(String.join(",", ids));
        log.info("{} users were deleted!", count);
    }

    @Transactional(readOnly = true)
    public Set<Power> findPowersByUserId(Long userId) {
        Set<Role> roles = this.findRolesByUserId(userId);
        Set<Power> powers = Sets.newHashSet();
        for (Role role : roles) {
            powers.addAll(findPowersByRole(role.getRoleId()));
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
            condition2.setPowerId(rolePower.getPowerId());
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
     * 根据多种ID获取用户信息
     */
    public User findByOpenId(String openId) {
        return userMapper.selectByOpenId(openId);
    }

    /**
     * 根据多种ID获取系统用户
     */
    public User findUserByIdentity(String identity) {
        return userMapper.selectByIdentity(identity);
    }

    /**
     * 根据多种ID获取用户信息（认证用）
     */
    public Map<String, String> findUserDetails(String identity) {
        User user = userMapper.selectByIdentity(identity);

        if (user != null) {
            Long uid = user.getUserId();
            Map<String, String> details = Maps.newHashMap();
            details.put("userId", uid.toString());
            details.put("username", user.getUsername());
            details.put("password", user.getPasswdHash());
            details.put("roles", findRolesByUserId(uid).stream().map(Role::getRoleName).distinct().collect(Collectors.joining(",")));
            details.put("perms", findPowersByUserId(uid).stream().map(Power::getPowerName).distinct().collect(Collectors.joining(",")));
            return details;
        }

        return null;
    }

    @Transactional(readOnly = true)
    public Paged<UserDetailDto> test(QueryBase condition) {
        return new Paged<>(userMapper.selectByCondition(condition), UserConverter.INSTANCE::toDto);
    }
}