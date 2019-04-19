package com.yodinfo.seed.dao;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.ConditionMapper;

@Component
public interface CommonMapper<T> extends BasicMapper<T>, ConditionMapper<T> {
}
