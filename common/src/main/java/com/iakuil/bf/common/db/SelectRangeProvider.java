package com.iakuil.bf.common.db;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.annotation.LogicDelete;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Map;

/**
 * 相等或者范围查询
 *
 * <p>注意：此处借用Example对象和SqlHelper工具类，不是完整的实现，仅支持大于小于等于。
 *
 * @author Kai
 */
public class SelectRangeProvider extends MapperTemplate {
    public SelectRangeProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectInRange(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        //支持查询指定列
        sql.append(SqlHelper.exampleSelectColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(exampleWhereClause());
        sql.append(SqlHelper.exampleForUpdate());
        return sql.toString();
    }

    public static String exampleWhereClause() {
        return "<if test=\"_parameter != null\">" +
                "<where>\n" +
                " ${@com.iakuil.bf.common.db.SelectRangeProvider@andNotLogicDelete(_parameter)}" +
                " <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n" +
                "  <foreach collection=\"oredCriteria\" item=\"criteria\">\n" +
                "    <if test=\"criteria.valid\">\n" +
                "      ${@com.iakuil.bf.common.db.SelectRangeProvider@appendAnd(criteria)}" +
                "      <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n" +
                "        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" +
                "          <when test=\"criterion.singleValue\">\n" +
                "            ${@com.iakuil.bf.common.db.SelectRangeProvider@appendAnd(criterion)} ${criterion.condition} #{criterion.value}\n" +
                "          </when>\n" +
                "        </foreach>\n" +
                "      </trim>\n" +
                "    </if>\n" +
                "  </foreach>\n" +
                " </trim>\n" +
                "</where>" +
                "</if>";
    }

    public static String appendAnd(Object parameter) {
        if (parameter instanceof Example.Criteria) {
            return ((Example.Criteria) parameter).getAndOr();
        } else if (parameter instanceof Example.Criterion) {
            return ((Example.Criterion) parameter).getAndOr();
        } else {
            return "and";
        }
    }

    public static String andNotLogicDelete(Object parameter) {
        String result = "";
        if (parameter instanceof Example) {
            Example example = (Example) parameter;
            Map<String, EntityColumn> propertyMap = example.getPropertyMap();

            for (Map.Entry<String, EntityColumn> entry : propertyMap.entrySet()) {
                EntityColumn column = entry.getValue();
                if (column.getEntityField().isAnnotationPresent(LogicDelete.class)) {
                    // 未逻辑删除的条件
                    result = column.getColumn() + " = " + SqlHelper.getLogicDeletedValue(column, false);

                    // 如果Example中有条件，则拼接" and "，
                    // 如果是空的oredCriteria，则where中只有逻辑删除注解的未删除条件
                    if (example.getOredCriteria() != null && example.getOredCriteria().size() != 0) {
                        result += " and ";
                    }
                }
            }
        }
        return result;
    }
}
