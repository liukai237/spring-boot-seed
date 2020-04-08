package com.yodinfo.seed.dao;

import com.yodinfo.seed.common.CrudMapper;
import com.yodinfo.seed.domain.Power;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@CacheNamespace
@Repository
public interface PowerMapper extends CrudMapper<Power> {
}
