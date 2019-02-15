package com.yodinfo.seed.service;

import com.github.pagehelper.PageHelper;
import com.yodinfo.seed.converter.UserConverter;
import com.yodinfo.seed.dao.UserMapper;
import com.yodinfo.seed.domain.User;
import com.yodinfo.seed.dto.BasicUserInfo;
import com.yodinfo.seed.dto.UserRegInfo;
import com.yodinfo.seed.util.HashIdUtils;
import com.yodinfo.seed.util.IdGen;
import com.yodinfo.seed.util.PasswordHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
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
    public BasicUserInfo findByUserName(String uid) {
        User user = userMapper.selectOne(User.builder().username(uid).build());
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

        long userId = new IdGen().nextId(); // 使用雪花算法生成ID
        user.setUserId(userId);
        user.setUsername(HashIdUtils.encrypt(System.currentTimeMillis())); // 使用HashIds生成唯一username（临时，用户可自行修改）
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        //String pwd = new SimpleHash("MD5", regInfo.getPassword(), ByteSource.Util.bytes(uid), 1024).toString();
        String pwd = null;
        try {
            pwd = PasswordHash.createHash(regInfo.getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace(); // should not be happened
        }
        LOGGER.debug("Encrypted passwd: {}", pwd);
        user.setPassword(pwd);
        return userMapper.insertSelective(user) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean modify(BasicUserInfo info) {
        User user = userConverter.toEntity(info);
        user.setUpdateTime(new Date());
        Condition condition = new Condition(User.class);
        condition.createCriteria().andCondition("username = '" + info.getUid() + "'");
        return userMapper.updateByConditionSelective(user, condition) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByUid(String... uids) {
        Condition condition = new Condition(User.class);
        condition.createCriteria().andIn("username", Arrays.asList(uids));
        int count = userMapper.deleteByCondition(condition);
        LOGGER.info("{} users were deleted!", count);
    }
}