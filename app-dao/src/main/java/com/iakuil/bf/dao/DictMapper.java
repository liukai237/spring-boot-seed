package com.iakuil.bf.dao;

import com.iakuil.bf.dao.entity.Dict;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 字典表
 */
@Repository
public interface DictMapper {

    Dict get(Long id);

    List<Dict> list(Dict query);

    int count(Map<String, Object> map);

    int save(Dict dict);

    int update(Dict dict);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<Dict> listType();
}
