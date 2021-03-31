package com.iakuil.bf.service;

import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.common.dict.DictCache;
import com.iakuil.bf.common.dict.DictEnum;
import com.iakuil.bf.dao.UserMapper;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.toolkit.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
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
        return mapper.insertList(entities) > entities.size();
    }

    private void preAdd(User entity) {
        // 如果username为空，则生成wxid开头的临时用户名
        entity.setUsername(StringUtils.defaultString(entity.getUsername(), "wxid_" + HASHIDS.encode(Long.parseLong(entity.getTel()))));
        entity.setPasswdHash(PasswordHash.createHash(entity.getPasswdHash()));
        entity.setVersion(1L);
    }

    /**
     * 根据ID、用户名、电话号码、邮箱等唯一标识获取用户信息（认证用）
     */
    @Transactional(readOnly = true)
    public User findByIdentity(String identity) {
        return userMapper.selectByIdentity(identity);
    }

}