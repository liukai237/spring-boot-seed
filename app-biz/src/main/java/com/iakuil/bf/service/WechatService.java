package com.iakuil.bf.service;

import com.github.pagehelper.PageHelper;
import com.iakuil.bf.common.PageData;
import com.iakuil.bf.dao.WxUserInfoMapper;
import com.iakuil.bf.dao.entity.WxUserInfo;
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

    public PageData<WxUserInfo> findWithPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(1, 0, "subscribe_time desc");
        return new PageData<>(wxUserInfoMapper.selectAll());
    }
}