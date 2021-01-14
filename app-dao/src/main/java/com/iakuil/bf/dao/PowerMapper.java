package com.iakuil.bf.dao;

import com.iakuil.bf.common.db.CrudMapper;
import com.iakuil.bf.dao.entity.Power;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace
public interface PowerMapper extends CrudMapper<Power> {
}
