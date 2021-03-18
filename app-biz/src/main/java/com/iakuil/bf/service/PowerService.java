package com.iakuil.bf.service;

import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.dao.RolePowerMapper;
import com.iakuil.bf.dao.entity.Power;
import com.iakuil.bf.dao.entity.RolePower;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限服务
 *
 * @author Kai
 */
@Slf4j
@Service
public class PowerService extends BaseService<Power> {

    private final RolePowerMapper rolePowerMapper;

    public PowerService(RolePowerMapper rolePowerMapper) {
        this.rolePowerMapper = rolePowerMapper;
    }

    @Transactional(readOnly = true)
    public Set<Power> findPowersByRoleId(Long roleId) {
        RolePower condition = new RolePower();
        condition.setRoleId(roleId);
        List<RolePower> rolePowers = rolePowerMapper.select(condition);

        if (CollectionUtils.isNotEmpty(rolePowers)) {
            return new HashSet<>(mapper.selectByIds(rolePowers.stream().map(String::valueOf).collect(Collectors.joining(","))));
        }
        return Collections.emptySet();
    }
}