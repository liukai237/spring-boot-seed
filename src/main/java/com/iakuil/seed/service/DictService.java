package com.iakuil.seed.service;

import com.iakuil.seed.common.Paged;
import com.iakuil.seed.dao.DictMapper;
import com.iakuil.seed.entity.Dict;
import com.iakuil.seed.util.MapBuilder;
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

    public List<Dict> list(Map<String, Object> map) {
        return dictMapper.list(map);
    }

    public Paged<Dict> listWithPage(Map<String, Object> param) {
        return new Paged<>(dictMapper.list(param));
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

    public String getName(String type, String value) {
        return dictMapper.list(MapBuilder.init()
                .append("type", type)
                .append("value", value)
                .build()).get(0).getName();
    }

    public List<Dict> listByType(String type) {
        return dictMapper.list(MapBuilder.init()
                .append("type", type)
                .build());
    }
}