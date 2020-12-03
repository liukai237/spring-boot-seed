package com.iakuil.bf.service;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.github.pagehelper.PageHelper;
import com.iakuil.bf.common.PageData;
import com.iakuil.bf.dao.DictMapper;
import com.iakuil.bf.dao.entity.Dict;
import com.iakuil.bf.service.converter.DictConverter;
import com.iakuil.bf.service.dto.DictDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 字典服务
 */
@Service
public class DictService {
    private final DictMapper dictMapper;
    private final IdGenerator idGenerator;

    @Autowired
    public DictService(DictMapper dictMapper, IdGenerator idGenerator) {
        this.dictMapper = dictMapper;
        this.idGenerator = idGenerator;
    }

    public Dict get(Long id) {
        return dictMapper.get(id);
    }

    public List<Dict> list(Dict dict) {
        return dictMapper.list(dict);
    }

    public PageData<DictDto> listWithPage(Integer pageNum, Integer pageSize, Dict param) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageData<>(dictMapper.list(param), DictConverter.INSTANCE::toDto);
    }

    public int count(Map<String, Object> map) {
        return dictMapper.count(map);
    }

    public int save(Dict dict) {
        Date now = new Date();
        dict.setId(idGenerator.generateId().longValue());
        dict.setCreateDate(now);
        dict.setUpdateDate(now);
        return dictMapper.save(dict);
    }

    public int update(Dict dict) {
        Date now = new Date();
        dict.setUpdateDate(now);
        return dictMapper.update(dict);
    }

    public int remove(Long id) {
        return dictMapper.remove(id);
    }

    public int batchRemove(Long[] ids) {
        return dictMapper.batchRemove(ids);
    }

    public List<Dict> listType() {
        return dictMapper.listType();
    }
}