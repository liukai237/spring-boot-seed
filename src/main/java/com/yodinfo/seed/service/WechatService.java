package com.yodinfo.seed.service;

import com.github.pagehelper.PageHelper;
import com.yodinfo.seed.bo.Paged;
import com.yodinfo.seed.dao.UserInfoMapper;
import com.yodinfo.seed.domain.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信相关服务
 */
@Slf4j
@Service
public class WechatService {

    private final UserInfoMapper userInfoMapper;

    public WechatService(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    public Paged<UserInfo> findWithPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "subscribeTime desc");
        return new Paged<>(userInfoMapper.selectAll());
    }
}