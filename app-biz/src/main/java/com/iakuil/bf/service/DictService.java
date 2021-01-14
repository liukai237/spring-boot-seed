package com.iakuil.bf.service;

import com.iakuil.bf.common.PageQuery;
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
public class DictService {
    private final DictMapper dictMapper;

    @Autowired
    public DictService(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    public Dict get(Long id) {
        return dictMapper.selectByPrimaryKey(id);
    }

    public List<Dict> list(Dict dict) {
        return dictMapper.select(dict);
    }

    public PageData<DictDto> listWithPage(Dict query) {
        return new PageData<>(dictMapper.select(query), DictConverter.INSTANCE::toDto);
    }

    public int save(Dict dict) {
        return dictMapper.insert(dict);
    }

    public int update(Dict dict) {
        return dictMapper.updateByPrimaryKey(dict);
    }

    public int remove(Long id) {
        return dictMapper.deleteByPrimaryKey(id);
    }

    public int batchRemove(String[] ids) {
        return dictMapper.deleteByIds(String.join(",", ids));
    }

    public List<Dict> listType() {
        return dictMapper.listType();
    }
}