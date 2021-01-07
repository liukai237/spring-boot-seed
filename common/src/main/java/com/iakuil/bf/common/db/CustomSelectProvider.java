package com.iakuil.bf.common.db;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * 自定义的通用Mapper组件
 *
 * @author Kai
 */
public class CustomSelectProvider extends MapperTemplate {
    public CustomSelectProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectMap(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(whereAllIfColumnsFromMap(entityClass));
        sql.append(SqlHelper.orderByDefault(entityClass));
        return sql.toString();
    }

    public String selectPage(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(whereAllIfColumnsFromPage(entityClass));
        sql.append(SqlHelper.orderByDefault(entityClass));
        return sql.toString();
    }

    public String whereAllIfColumnsFromMap(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columns) {
            sql.append("<if test=\"");
            if (column.getJavaType().equals(String.class)) {
                sql.append(column.getProperty() + " != null and " + column.getProperty() + " != ''\">");
            } else {
                sql.append(column.getProperty() + " != null\">");
            }
            sql.append(column.getColumn() + " = #{" + column.getProperty() + "} ");
            sql.append("</if>");
        }

        sql.append("</where>");
        return sql.toString();
    }

    public String whereAllIfColumnsFromPage(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columns) {
            sql.append("<if test=\"");
            if (column.getJavaType().equals(String.class)) {
                sql.append("condition." + column.getProperty() + " != null and condition." + column.getProperty() + " != ''\">");
            } else {
                sql.append("condition." + column.getProperty() + " != null\">");
            }
            sql.append(column.getColumn() + " = #{condition." + column.getProperty() + "} ");
            sql.append("</if>");
        }

        sql.append("</where>");
        return sql.toString();
    }
}
