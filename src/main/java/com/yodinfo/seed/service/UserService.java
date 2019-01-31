package com.yodinfo.seed.service;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.yodinfo.seed.converter.UserConverter;
import com.yodinfo.seed.dao.UserMapper;
import com.yodinfo.seed.domain.User;
import com.yodinfo.seed.dto.BasicUserInfo;
import com.yodinfo.seed.dto.UserRegInfo;
import com.yodinfo.seed.util.IdGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final UserConverter userConverter;

    public UserService(UserMapper userMapper, UserConverter userConverter) {
        this.userMapper = userMapper;
        this.userConverter = userConverter;
    }

    @Transactional(readOnly = true)
    public BasicUserInfo findById(Long userId) {
        User user = userMapper.selectOne(User.builder().userId(userId).build());
        return userConverter.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<BasicUserInfo> findWithPaging(Integer pageNum, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        return userConverter.toDtoList(userMapper.selectAll());
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean add(UserRegInfo regInfo) {
        User user = userConverter.toEntity(regInfo);
        user.setUserId(new IdGen().nextId());
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        if (Strings.isNullOrEmpty(user.getUsername())) {
            user.setUsername("手机用户" + user.getPhone());
        }
        return userMapper.insert(user) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean modify(BasicUserInfo info) {
        User user = userConverter.toEntity(info);
        user.setUpdateTime(new Date());
        return userMapper.updateByPrimaryKeySelective(user) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        return userMapper.deleteByPrimaryKey(User.builder().userId(id).build()) > 0;
    }
}