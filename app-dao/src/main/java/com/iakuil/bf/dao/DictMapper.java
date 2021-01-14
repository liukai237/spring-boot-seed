package com.iakuil.bf.dao;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.dao.entity.Dict;

import java.util.List;

/**
 * 字典表
 */
public interface DictMapper extends CrudMapper<Dict> {
    List<Dict> listType();
}
