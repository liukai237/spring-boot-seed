package com.iakuil.seed.support;

import tk.mybatis.mapper.genid.GenId;

/**
 * UUID主键生成器
 * <p>采用UUID方案生成主键（不带中划线）。</p><br/>
 * <p>使用方法：Entity ID字段上增加注解{@code @KeySql(genId = UuidGenId.class)}</p><br/>
 * <p>注意：genId是所有方式中优先级最低的！</p>
 */
public class UuidGenId implements GenId<String> {
    @Override
    public String genId(String table, String column) {
        return Strings.getUuidStr();
    }
}