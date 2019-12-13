package com.yodinfo.seed.service;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.yodinfo.seed.bo.PageData;
import com.yodinfo.seed.converter.UserConverter;
import com.yodinfo.seed.dao.*;
import com.yodinfo.seed.domain.*;
import com.yodinfo.seed.dto.BasicUserInfo;
import com.yodinfo.seed.dto.UserRegInfo;
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
    private final UserConverter userConverter;

    public UserService(UserMapper userMapper, RoleMapper roleMapper, PowerMapper powerMapper, RolePowerMapper rolePowerMapper, UserRoleMapper userRoleMapper, UserConverter userConverter) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.powerMapper = powerMapper;
        this.rolePowerMapper = rolePowerMapper;
        this.userRoleMapper = userRoleMapper;
        this.userConverter = userConverter;
    }

    @Transactional(readOnly = true)
    public BasicUserInfo findByUserName(String uid) {
        User user = userMapper.selectOne(User.builder().username(uid).build());
        return userConverter.toDto(user);
    }

    @Transactional(readOnly = true)
    public User findByUserName2(String uid) {
        return userMapper.selectOne(User.builder().username(uid).build());
    }

    @Transactional(readOnly = true)
    public PageData<BasicUserInfo> findWithPaging(Integer pageNum, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        return new PageData<>(userMapper.selectAll(), UserConverter.INSTANCE::toDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean add(UserRegInfo regInfo) {
        User user = userConverter.toEntity(regInfo);

        long userId = new IdGen().nextId(); // 使用雪花算法生成ID
        user.setUserId(userId);
        user.setUsername(HashIdUtils.encrypt(System.currentTimeMillis())); // 使用HashIds生成唯一username（临时，用户可自行修改）
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        //String pwdHash = new SimpleHash("MD5", regInfo.getPassword(), ByteSource.Util.bytes(uid), 1024).toString();
        String pwdHash = null;
        try {
            pwdHash = PasswordHash.createHash(regInfo.getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage()); // should not be happened
        }
        log.debug("Encrypted passwd: {}", pwdHash);
        user.setPasswdHash(pwdHash);
        return userMapper.insertSelective(user) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean modify(BasicUserInfo info) {
        User user = userConverter.toEntity(info);
        user.setUpdateTime(new Date());

        //TODO 修改密码字段及防止SQL注入
        Condition condition = new Condition(User.class);
        condition.createCriteria().andCondition("username = '" + info.getUid() + "'");
        return userMapper.updateByConditionSelective(user, condition) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByUsernames(String... usernames) {
        Condition condition = new Condition(User.class);
        condition.createCriteria().andIn("username", Arrays.asList(usernames));
        int count = userMapper.deleteByCondition(condition);
        log.info("{} users were deleted!", count);
    }

    @Transactional(readOnly = true)
    public List<Power> findPowersByUserId(Long userId) {
        List<Role> roles = this.findRolesByUserId(userId);
        List<Power> powers = Lists.newArrayList();
        for (Role role : roles) {
            powers.addAll(findPowersByRole(role.getRoleId()));
        }

        return powers;
    }

    @Transactional(readOnly = true)
    public List<Power> findPowersByRole(Long roleId) {
        Condition condition = new Condition(RolePower.class);
        condition.createCriteria().andEqualTo("roleId", roleId);
        List<RolePower> rolePowers = rolePowerMapper.selectByCondition(condition);

        List<Power> powers = Lists.newArrayList();
        for (RolePower rolePower : rolePowers) {
            Condition cond = new Condition(RolePower.class);
            cond.createCriteria().andEqualTo("powerId", rolePower.getPowerId());
            powers.addAll(powerMapper.selectByCondition(cond));
        }

        return powers;
    }

    @Transactional(readOnly = true)
    public List<Role> findRolesByUserId(Long userId) {
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
            return roleMapper.selectByCondition(condition);
        }

        return Collections.emptyList();
    }
}