package com.iakuil.bf.service;

import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.dao.DictMapper;
import com.iakuil.bf.dao.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典服务
 *
 * @author Kai
 */
@Service
public class DictService extends BaseService<Dict> {
    private final DictMapper dictMapper;

    @Autowired
    public DictService(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    public List<Dict> listType() {
        return dictMapper.listType();
    }
}