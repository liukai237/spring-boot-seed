package com.iakuil.bf.service;

import com.iakuil.bf.common.PageData;
import com.iakuil.bf.dao.DictMapper;
import com.iakuil.bf.dao.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DictService {
    private final DictMapper dictMapper;

    @Autowired
    public DictService(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    public Dict get(Long id) {
        return dictMapper.get(id);
    }

    public List<Dict> list(Dict dict) {
        return dictMapper.list(dict);
    }

    public PageData<Dict> listWithPage(Dict param) {
        return new PageData<>(dictMapper.list(param));
    }

    public int count(Map<String, Object> map) {
        return dictMapper.count(map);
    }

    public int save(Dict dict) {
        return dictMapper.save(dict);
    }

    public int update(Dict dict) {
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