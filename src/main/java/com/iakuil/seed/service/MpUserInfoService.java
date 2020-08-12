package com.iakuil.seed.service;

import com.iakuil.seed.constant.Constant;
import com.iakuil.seed.dao.MpUserInfoMapper;
import com.iakuil.seed.domain.MpUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class MpUserInfoService {

    private final MpUserInfoMapper mpUserInfoMapper;

    public MpUserInfoService(MpUserInfoMapper mpUserInfoMapper) {
        this.mpUserInfoMapper = mpUserInfoMapper;
    }

    public MpUserInfo findById(String openId) {
        return mpUserInfoMapper.selectByPrimaryKey(openId);
    }

    public MpUserInfo findByUnionId(String unionId) {
        MpUserInfo query = new MpUserInfo();
        query.setUnionId(unionId);
        return mpUserInfoMapper.selectOne(query);
    }

    public List<MpUserInfo> findByCondition(MpUserInfo condition) {
        return mpUserInfoMapper.select(condition);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addOrModifyUserInfo(MpUserInfo newRecord) {
        String openId = newRecord.getOpenId();
        MpUserInfo dbRecord = mpUserInfoMapper.selectByPrimaryKey(openId);
        if (dbRecord == null) {
            newRecord.setSource(Constant.DEFAULT_INDEX);
            return mpUserInfoMapper.insert(newRecord) > 0;
        } else {
            return mpUserInfoMapper.updateByPrimaryKeySelective(newRecord) > 0;
        }
    }
}
