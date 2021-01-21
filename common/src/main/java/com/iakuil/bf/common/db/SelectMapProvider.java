package com.iakuil.bf.common.db;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * 通过Map进行查询
 *
 * @author Kai
 */
public class SelectMapProvider extends MapperTemplate {
    public SelectMapProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
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
}
