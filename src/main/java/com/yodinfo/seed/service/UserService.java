package com.yodinfo.seed.service;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yodinfo.seed.bo.Paged;
import com.yodinfo.seed.converter.UserConverter;
import com.yodinfo.seed.dao.*;
import com.yodinfo.seed.domain.*;
import com.yodinfo.seed.dto.BasicUserInfo;
import com.yodinfo.seed.util.HashIdUtils;
import com.yodinfo.seed.util.IdGen;
import com.yodinfo.seed.util.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

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

    @Transactional(readOnly = true)
    public Paged<BasicUserInfo> findWithPaging(Integer pageNum, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        return new Paged<>(userMapper.selectAll(), UserConverter.INSTANCE::toDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean add(User user) {
        long userId = new IdGen().nextId(); // 使用雪花算法生成ID
        user.setUserId(userId);
        user.setUsername(HashIdUtils.encrypt(System.currentTimeMillis())); // 使用HashIds生成唯一username（临时，用户可自行修改）

        //String pwdHash = new SimpleHash("MD5", regInfo.getPassword(), ByteSource.Util.bytes(uid), 1024).toString();
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
        return userMapper.updateByPrimaryKeySelective(user) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String... ids) {
        Condition condition = new Condition(User.class);
        condition.createCriteria().andIn("userId", Arrays.asList(ids));
        int count = userMapper.deleteByCondition(condition);
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
        Condition condition = new Condition(RolePower.class);
        condition.createCriteria().andEqualTo("roleId", roleId);
        List<RolePower> rolePowers = rolePowerMapper.selectByCondition(condition);

        Set<Power> powers = Sets.newHashSet();
        for (RolePower rolePower : rolePowers) {
            Condition cond = new Condition(RolePower.class);
            cond.createCriteria().andEqualTo("powerId", rolePower.getPowerId());
            powers.addAll(powerMapper.selectByCondition(cond));
        }

        return powers;
    }

    @Transactional(readOnly = true)
    public Set<Role> findRolesByUserId(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        Set<Long> roleIds = null;
        if (user != null) {
            Condition condition = new Condition(UserRole.class);
            condition.createCriteria().andEqualTo("userId", userId);
            List<UserRole> userRoles = userRoleMapper.selectByCondition(condition);
            roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        }

        if (CollectionUtils.isNotEmpty(roleIds)) {
            Condition condition = new Condition(Role.class);
            condition.createCriteria().andIn("roleId", roleIds);
            return new HashSet<>(roleMapper.selectByCondition(condition));
        }

        return Collections.emptySet();
    }

    public Map<String, String> findUserDetails(String identity) {
        Condition condition = new Condition(User.class);
        condition.createCriteria()
                .orEqualTo("userId", identity)
                .orEqualTo("username", identity)
                .orEqualTo("tel", identity)
                .orEqualTo("email", identity);
        List<User> users = userMapper.selectByCondition(condition);

        if (CollectionUtils.isNotEmpty(users)) {
            User user = users.get(0);
            Long uid = user.getUserId();
            Map<String, String> details = Maps.newHashMap();
            details.put("uid", uid.toString());
            details.put("roles", findRolesByUserId(uid).stream().map(Role::getRoleName).distinct().collect(Collectors.joining(",")));
            details.put("perms", findPowersByUserId(uid).stream().map(Power::getPowerName).distinct().collect(Collectors.joining(",")));
            return details;
        }

        return null;
    }
}