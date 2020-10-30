package com.iakuil.seed.common.db;

import com.iakuil.seed.dao.UidSequenceMapper;
import com.iakuil.seed.common.tool.ApplicationContextHolder;
import tk.mybatis.mapper.genid.GenId;

/**
 * 默认主键ID生成器
 * <p>采用Flickr GUID方案。</p><br/>
 * <p>使用方法：Entity ID字段上增加注解{@code @KeySql(genId = DefaultGenId.class)}</p><br/>
 * <p>注意：Sharding JDBC场景下有Bug</p>
 */
public class DefaultGenId implements GenId<Long> {
    @Override
    public Long genId(String s, String s1) {
        UidSequenceMapper.UidSequence uidSequence = new UidSequenceMapper.UidSequence();
        ApplicationContextHolder.getBean(UidSequenceMapper.class).getUid(uidSequence);
        return uidSequence.getId();
    }
}
