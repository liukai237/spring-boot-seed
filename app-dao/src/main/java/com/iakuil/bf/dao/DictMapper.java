package com.iakuil.bf.dao;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.dao.entity.Dict;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典表
 */
@Repository
public interface DictMapper extends CrudMapper<Dict> {
    List<Dict> listType();
}
