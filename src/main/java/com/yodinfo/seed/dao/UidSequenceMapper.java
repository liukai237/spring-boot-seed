package com.yodinfo.seed.dao;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;

/**
 * LAST_INSERT_ID方式生成全局唯一ID
 */
@Mapper
public interface UidSequenceMapper {

    @Insert("REPLACE INTO uid_sequence (stub) VALUES('x')")
    @SelectKey(statement = {"SELECT LAST_INSERT_ID()"}, keyProperty = "uidSequence.id", before = false, resultType = long.class)
    int getUid(@Param("uidSequence") UidSequence uidSequence);

    @Getter
    @Setter
    class UidSequence {
        private Long id;
        private String stub;
    }
}
