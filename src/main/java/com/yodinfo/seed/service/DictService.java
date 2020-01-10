package com.yodinfo.seed.service;

import com.yodinfo.seed.common.Paged;
import com.yodinfo.seed.dao.DictMapper;
import com.yodinfo.seed.domain.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public Paged<Dict> listWithPaging(Map<String, Object> param) {
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
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        param.put("value", value);
        String rString = dictMapper.list(param).get(0).getName();
        return rString;
    }

    public List<Dict> listByType(String type) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", type);
        return dictMapper.list(param);
    }
}