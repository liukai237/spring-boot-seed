package com.iakuil.bf.common.db;

import com.iakuil.bf.common.exception.BusinessException;
import com.iakuil.bf.common.tool.JsonPathUtils;
import com.iakuil.bf.common.tool.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonColumnTypeHandler extends BaseTypeHandler<Object> {
    private static final String CLASS_HOLDER_FIELD = "realType";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        String json = JsonUtils.bean2Json(parameter);
        String startToken = json.startsWith("[") ? "[{" : "{";
        ps.setString(i, startToken + "\"" + CLASS_HOLDER_FIELD + "\":\"" + parameter.getClass().getName() + "\"," + StringUtils.substringAfter(json, startToken));
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String data = rs.getString(columnName);
        return StringUtils.isBlank(data) ? null : getJavaTypeFromJson(data);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String data = rs.getString(columnIndex);
        return StringUtils.isBlank(data) ? null : getJavaTypeFromJson(data);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String data = cs.getString(columnIndex);
        return StringUtils.isBlank(data) ? null : getJavaTypeFromJson(data);
    }

    private Object getJavaTypeFromJson(String json) {
        String realType = JsonPathUtils.readStr(json, (json.startsWith("[") ? "$[0]." : "$.") + CLASS_HOLDER_FIELD);
        if (realType == null) {
            return null;
        }

        Class<?> clazz;
        try {
            clazz = Class.forName(realType);
        } catch (ClassNotFoundException e) {
            throw new BusinessException("Invalid class type: " + realType);
        }

        return JsonUtils.json2bean(json, clazz);
    }
}
