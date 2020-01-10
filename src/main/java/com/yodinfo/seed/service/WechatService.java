package com.yodinfo.seed.service;

import com.github.pagehelper.PageHelper;
import com.yodinfo.seed.common.Paged;
import com.yodinfo.seed.dao.WxUserInfoMapper;
import com.yodinfo.seed.domain.WxUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信相关服务
 */
@Slf4j
@Service
public class WechatService {

    private final WxUserInfoMapper wxUserInfoMapper;

    public WechatService(WxUserInfoMapper wxUserInfoMapper) {
        this.wxUserInfoMapper = wxUserInfoMapper;
    }

    public List<WxUserInfo> findAll() {
        return wxUserInfoMapper.selectAll();
    }

    public Paged<WxUserInfo> findWithPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "subscribeTime desc");
        return new Paged<>(wxUserInfoMapper.selectAll());
    }
}