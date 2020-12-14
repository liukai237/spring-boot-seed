package com.iakuil.bf.common.db;

import com.iakuil.bf.common.tool.JsonPathUtils;
import com.iakuil.bf.common.tool.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Slf4j
public class JsonColumnTypeHandler extends BaseTypeHandler<Object> {
    private static final String AGGREGATION_MODE = "aggregationMode";
    private static final String CLASS_HOLDER_FIELD = "realJavaType";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        String json = JsonUtils.bean2Json(parameter);
        String extInfo = "";
        if (parameter instanceof List) {
            List list = (List) parameter;
            if (list.size() > 0) {
                extInfo = "\"" + AGGREGATION_MODE + "\":\"" + AggregationMode.LIST.name() + "\","
                        + "\"" + CLASS_HOLDER_FIELD + "\":\"" + (list).get(0).getClass().getName() + "\",";
            }
        } else if (parameter instanceof Set) {
            Set set = (Set) parameter;
            if (set.size() > 0) {
                extInfo = "\"" + AGGREGATION_MODE + "\":\"" + AggregationMode.SET.name() + "\","
                        + "\"" + CLASS_HOLDER_FIELD + "\":\"" + ((Set) parameter).stream().findFirst().get().getClass().getName() + "\",";
            }
        } else if (parameter.getClass().getName().startsWith("[L")) {
            extInfo = "\"" + AGGREGATION_MODE + "\":\"" + AggregationMode.ARRAY.name() + "\","
                    + "\"" + CLASS_HOLDER_FIELD + "\":\"" + parameter.getClass().getName() + "\",";
        } else {
            extInfo = "\"" + AGGREGATION_MODE + "\":\"" + AggregationMode.BEAN.name() + "\","
                    + "\"" + CLASS_HOLDER_FIELD + "\":\"" + parameter.getClass().getName() + "\",";
        }

        String startToken = json.startsWith("[") ? "[{" : "{";
        ps.setString(i, startToken + extInfo + StringUtils.substringAfter(json, startToken));
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

        Object result;
        String mode = JsonPathUtils.readStr(json, (json.startsWith("[") ? "$[0]." : "$.") + AGGREGATION_MODE);
        AggregationMode am = AggregationMode.valueOf(mode);
        if (am == AggregationMode.LIST) {
            result = JsonUtils.json2List(json, getClassByName(realType));
        } else if (am == AggregationMode.SET) {
            result = JsonUtils.json2Set(json, getClassByName(realType));
        } else {
            result = JsonUtils.json2bean(json, getClassByName(realType));
        }

        return result;
    }

    private Class<?> getClassByName(String name) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(name);
        } catch (ClassNotFoundException e) {
            // 不要随意变更包名类名
            log.warn("Invalid class type: " + name);
        }
        return clazz;
    }

    private enum AggregationMode {
        BEAN, ARRAY, LIST, SET;
    }
}
