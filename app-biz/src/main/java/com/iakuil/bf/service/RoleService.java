package com.iakuil.bf.service;

import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.dao.UserRoleMapper;
import com.iakuil.bf.dao.entity.Role;
import com.iakuil.bf.dao.entity.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色服务
 *
 * @author Kai
 */
@Slf4j
@Service
public class RoleService extends BaseService<Role> {

    private final UserRoleMapper userRoleMapper;

    public RoleService(UserRoleMapper userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
    }

    @Transactional(readOnly = true)
    public Set<Role> findRolesByUserId(Long userId) {
        UserRole condition = new UserRole();
        condition.setUserId(userId);
        Set<Long> roleIds = userRoleMapper.select(condition).stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(roleIds)) {
            return new HashSet<>(mapper.selectByIds(roleIds.stream().map(String::valueOf).collect(Collectors.joining(","))));
        }
        return Collections.emptySet();
    }

    public void correlationPermissions(Long roleId, Long... permissionIds) {
    }

    public void uncorrelationPermissions(Long roleId, Long... permissionIds) {
    }
}
