package com.iakuil.seed.service;

import com.iakuil.seed.common.Paged;
import com.iakuil.seed.dao.WxUserInfoMapper;
import com.iakuil.seed.domain.WxUserInfo;
import com.iakuil.seed.annotation.StartPage;
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

    @StartPage(orderBy = "subscribeTime desc")
    public Paged<WxUserInfo> findWithPage(Integer pageNum, Integer pageSize) {
        return new Paged<>(wxUserInfoMapper.selectAll());
    }
}