package com.iakuil.seed.support;

import com.iakuil.seed.dao.UidSequenceMapper;
import tk.mybatis.mapper.genid.GenId;

/**
 * 默认主键ID生成器
 * <p>使用方法：Entity ID字段上增加注解{@code @KeySql(genId = DefaultGenId.class)}</p><br/>
 * <p>注意：genId是所有方式中优先级最低的！</p>
 */
public class DefaultGenId implements GenId<Long> {
    @Override
    public Long genId(String s, String s1) {
        UidSequenceMapper.UidSequence uidSequence = new UidSequenceMapper.UidSequence();
        ApplicationContextHolder.getBean(UidSequenceMapper.class).getUid(uidSequence);
        return uidSequence.getId();
    }
}
