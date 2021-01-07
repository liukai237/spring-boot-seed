package com.iakuil.bf.common.db;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.iakuil.bf.common.tool.PluginUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;

import java.util.ArrayList;
import java.util.List;

/**
 * 逻辑删除SqlSource
 *
 * @author Kai
 */
public class LogicallyDeletedDynamicSqlSource implements SqlSource {

    private final DbType dbType;
    private final SqlSource source;

    private final String logicDeleteField;
    private final String logicDeleteValue;
    private final String logicNotDeleteValue;

    public LogicallyDeletedDynamicSqlSource(DbType dbType, SqlSource source, String logicDeleteField, String logicDeleteValue, String logicNotDeleteValue) {
        this.dbType = dbType;
        this.source = source;
        this.logicDeleteField = logicDeleteField;
        this.logicDeleteValue = logicDeleteValue;
        this.logicNotDeleteValue = logicNotDeleteValue;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = source.getBoundSql(parameterObject);
        PluginUtils.setFieldValue(boundSql, "sql", logicallyDeleted(boundSql.getSql()));
        return boundSql;
    }

    public String logicallyDeleted(String sql) {
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        List<SQLStatement> stmtList = parser.parseStatementList();
        SQLStatement stmt = stmtList.get(0);

        // 删除语句
        if (stmt instanceof SQLDeleteStatement) {
            SQLDeleteStatement sstmt = (SQLDeleteStatement) stmt;
            return deleteToUpdate(sstmt);
        }
        // 查询语句
        else if (stmt instanceof SQLSelectStatement) {
            SQLSelectStatement sstmt = (SQLSelectStatement) stmt;
            return selectAddWhere(sstmt);
        }
        return sql;
    }

    /**
     * 删除语句变更新字段语
     */
    private String deleteToUpdate(SQLDeleteStatement deleteStatement) {
        StringBuffer buf = new StringBuffer();
        buf.append("UPDATE ");

        deleteStatement.getTableSource().output(buf);
        buf.append(" SET ");
        {
            List<String> list = getFrom(deleteStatement.getTableSource());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        buf.append(", ");
                    }
                    String as = list.get(i);
                    StringBuffer whereSql = new StringBuffer();
                    if (as != null && as.length() > 0) {
                        whereSql.append(as);
                        whereSql.append(".");
                    }
                    whereSql.append(logicDeleteField);
                    whereSql.append(" = ");
                    whereSql.append(logicDeleteValue);
                    buf.append(whereSql);
                }
            }

        }

        if (deleteStatement.getWhere() != null) {
            buf.append(" WHERE ");
            SQLExpr opExpr = deleteStatement.getWhere();
            buf.append(SQLUtils.toSQLString(opExpr));
        }
        return buf.toString();
    }

    /**
     * 给Select加上删除where
     */
    private String selectAddWhere(SQLSelectStatement sstmt) {

        SQLSelect sqlselect = sstmt.getSelect();
        SQLSelectQueryBlock query = (SQLSelectQueryBlock) sqlselect.getQuery();

        if (query != null && query.getFrom() != null) {
            List<String> list = getFrom(query.getFrom());
            if (list != null) {
                for (String as : list) {
                    StringBuffer whereSql = new StringBuffer();
                    if (as != null && as.length() > 0) {
                        whereSql.append(as);
                        whereSql.append(".");
                    }
                    whereSql.append(logicDeleteField);
                    whereSql.append(" = ");
                    whereSql.append(logicNotDeleteValue);
                    SQLExpr expr = new SQLExprParser(whereSql.toString(), dbType).expr();
                    query.addWhere(expr);
                }
            }
        }
        return sstmt.toString();
    }

    /**
     * 查询字段alias值，没有返回NUll
     */
    private List<String> getFrom(SQLTableSource tableFrom) {
        StringBuffer buffer = new StringBuffer();
        tableFrom.accept(new SQLASTOutputVisitor(buffer));
        String tables = buffer.toString();

        tables = tables.split("\n")[0];

        List<String> list = new ArrayList<String>();
        if (buffer.length() <= 0) {
            return null;
        }

        String[] tabs = tables.split(", ");
        for (String tab : tabs) {
            String[] names = tab.split(" ");
            if (names.length > 1) {
                list.add(names[1]);
            } else {
                list.add(null);
            }
        }
        return list;
    }
}
