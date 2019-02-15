package com.yodinfo.seed.dao;

import tk.mybatis.mapper.common.ConditionMapper;

public interface CommonMapper<T> extends BasicMapper<T>, ConditionMapper<T> {
}
