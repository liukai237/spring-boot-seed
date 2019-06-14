package com.yodinfo.seed.common;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.ConditionMapper;

@Component
public interface CommonMapper<T> extends BasicMapper<T>, ConditionMapper<T> {
}
