package com.yodinfo.seed.dao;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

public interface BasicMapper<T> extends Mapper<T>, IdsMapper<T> {
}