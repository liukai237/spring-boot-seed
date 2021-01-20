package com.iakuil.bf.service;

import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.common.PageData;
import com.iakuil.bf.dao.DictMapper;
import com.iakuil.bf.dao.entity.Dict;
import com.iakuil.bf.service.converter.DictConverter;
import com.iakuil.bf.service.dto.DictDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典服务
 */
@Service
public class DictService extends BaseService<Dict> {
    private final DictMapper dictMapper;

    @Autowired
    public DictService(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    public PageData<DictDto> listWithPage(Dict query) {
        return new PageData<>(dictMapper.select(query), DictConverter.INSTANCE::toDto);
    }

    public List<Dict> listType() {
        return dictMapper.listType();
    }
}