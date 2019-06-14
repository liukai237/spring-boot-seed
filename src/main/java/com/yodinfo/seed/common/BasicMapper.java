package com.yodinfo.seed.common;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

public interface BasicMapper<T> extends Mapper<T>, IdsMapper<T> {
}