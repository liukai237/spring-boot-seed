package com.iakuil.bf.common.db;

import com.iakuil.bf.common.dict.DictEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MyBatis枚举处理
 *
 * <p>自动选择枚举处理器:字典枚举使用{@link DictEnumTypeHandler}，其他枚举使用默认的{@link EnumTypeHandler}
 *
 * @author Kai
 */
public class AutoEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private BaseTypeHandler typeHandler;

    public AutoEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        if (DictEnum.class.isAssignableFrom(type)) {
            typeHandler = new DictEnumTypeHandler(type);
        } else {
            typeHandler = new EnumTypeHandler<>(type);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        typeHandler.setNonNullParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return (E) typeHandler.getNullableResult(rs, columnName);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return (E) typeHandler.getNullableResult(rs, columnIndex);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (E) typeHandler.getNullableResult(cs, columnIndex);
    }
}
