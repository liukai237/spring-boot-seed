package com.iakuil.seed.support;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import tk.mybatis.mapper.genid.GenId;

/**
 * Self ID生成器
 * <p>采用Sharding JDBC Self ID Generator方案。</p>
 */
public class SelfGenId implements GenId<Long> {
    @Override
    public Long genId(String s, String s1) {
        return ApplicationContextHolder.getBean(IdGenerator.class).generateId().longValue();
    }
}
