package com.iakuil.bf.common.db;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.iakuil.bf.common.tool.ApplicationContextHolder;
import tk.mybatis.mapper.genid.GenId;

/**
 * 自定义的ID发号器（基于当当开源的发号器）
 *
 * @author Kai
 */
public class CommonSelfGenId implements GenId<Long> {

    @Override
    public Long genId(String table, String column) {
        return ApplicationContextHolder.getBean(IdGenerator.class).generateId().longValue();
    }
}
