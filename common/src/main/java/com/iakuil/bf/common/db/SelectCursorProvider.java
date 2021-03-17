package com.iakuil.bf.common.db;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Iterator;
import java.util.Set;

/**
 * 游标查询（试验功能）
 *
 * @author Kai
 */
public class SelectCursorProvider extends MapperTemplate {

    public SelectCursorProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectByNextId(MappedStatement ms) {

        Class<?> entityClass = this.getEntityClass(ms);
        this.setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id");
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(whereAllIfColumns(entityClass, this.isNotEmpty(), false));
        sql.append(SqlHelper.orderByDefault(entityClass));

        return sql.toString();
    }

    public static String whereAllIfColumns(Class<?> entityClass, boolean empty, boolean useVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        sql.append("id &gt; ${nextId}");
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        Iterator var5 = columnSet.iterator();

        while (true) {
            EntityColumn column;
            do {
                if (!var5.hasNext()) {
                    if (useVersion) {
                        sql.append(SqlHelper.whereVersion(entityClass));
                    }

                    sql.append("</where>");
                    return sql.toString();
                }

                column = (EntityColumn) var5.next();
            } while (useVersion && column.getEntityField().isAnnotationPresent(Version.class));

            if (!column.isId()) {
                sql.append(getIfNotNull(column, " AND " + getColumnEqualsHolder(column), empty));
            }
        }
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
