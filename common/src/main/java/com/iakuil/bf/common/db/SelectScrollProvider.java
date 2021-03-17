package com.iakuil.bf.common.db;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * 滚动查询（试验功能）
 *
 * @author Kai
 */
public class SelectScrollProvider extends MapperTemplate {

    public SelectScrollProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectByScrollId(MappedStatement ms) {

        Class<?> entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id");
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(whereAllIfColumns(entityClass, this.isNotEmpty()));
        // 处理orderBy和pageSize
        sql.append("<if test=\"record.orderBy != null and record.orderBy != ''\"> ORDER BY ${record.orderBy}</if>");
        sql.append("<choose>");
        sql.append("<when test=\"record.pageSize !=null and record.pageSize &lt; 10000\">");
        sql.append(" LIMIT ${record.pageSize}");
        sql.append("</when>");
        sql.append("<otherwise> LIMIT 10000</otherwise>");
        sql.append("</choose>");

        return sql.toString();
    }

    public static String whereAllIfColumns(Class<?> entityClass, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        sql.append("id &gt; ${scrollId}");

        //获取逻辑删除列
        EntityColumn logicDeleteColumn = SqlHelper.getLogicDeleteColumn(entityClass);

        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnSet) {
            if (column.isId()) {
                continue;
            }

            if (logicDeleteColumn != null && logicDeleteColumn == column) {
                sql.append(" AND " + column.getColumn() + " = " + SqlHelper.getLogicDeletedValue(column, false));
                continue;
            }

            sql.append(getIfNotNull(column, " AND " + getColumnEqualsHolder(column), empty));
        }

        sql.append("</where>");
        return sql.toString();
    }

    public static String getIfNotNull(EntityColumn column, String contents, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"");

        sql.append("record.").append(column.getProperty()).append(" != null");
        if (empty && column.getJavaType().equals(String.class)) {
            sql.append(" and ").append(column.getProperty()).append(" != '' ");
        }

        sql.append("\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }

    public static String getColumnEqualsHolder(EntityColumn column) {
        return column.getColumn() + " = " + getColumnHolder(column);
    }

    public static String getColumnHolder(EntityColumn column) {

        StringBuffer sb = new StringBuffer("#{");

        sb.append("record.").append(column.getProperty());

        if (column.getJdbcType() != null) {
            sb.append(", jdbcType=");
            sb.append(column.getJdbcType().toString());
        }

        if (column.getTypeHandler() != null) {
            sb.append(", typeHandler=");
            sb.append(column.getTypeHandler().getCanonicalName());
        }

        if (column.isUseJavaType() && !column.getJavaType().isArray()) {
            sb.append(", javaType=");
            sb.append(column.getJavaType().getCanonicalName());
        }

        sb.append("}");

        return sb.toString();
    }
}
