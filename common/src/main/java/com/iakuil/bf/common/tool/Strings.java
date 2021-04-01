package com.iakuil.bf.common.tool;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * <p>部分工具类参考自hutool
 *
 * @author Kai
 */
@UtilityClass
public class Strings {

    private static final Pattern PATTERN_NONE_COMMA = Pattern.compile("^([\\w_]+[\\+\\-]{1})+$");
    private static final Pattern PATTERN_WITH_COMMA = Pattern.compile("^([\\+\\-]+[\\w_]*,)+[\\+\\-][\\w_]*$");

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    public static final int INDEX_NOT_FOUND = -1;
    public static final char C_SPACE = ' ';
    public static final char C_TAB = '\t';
    public static final char C_DOT = '.';
    public static final char C_SLASH = '/';
    public static final char C_BACKSLASH = '\\';
    public static final char C_CR = '\r';
    public static final char C_LF = '\n';
    public static final char C_UNDERLINE = '_';
    public static final char C_COMMA = ',';
    public static final char C_DELIM_START = '{';
    public static final char C_DELIM_END = '}';
    public static final char C_BRACKET_START = '[';
    public static final char C_BRACKET_END = ']';
    public static final char C_COLON = ':';
    public static final char C_AT = '@';
    public static final String SPACE = " ";
    public static final String TAB = "\t";
    public static final String DOT = ".";
    public static final String DOUBLE_DOT = "..";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String EMPTY = "";
    public static final String NULL = "null";
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = "\r\n";
    public static final String UNDERLINE = "_";
    public static final String DASHED = "-";
    public static final String COMMA = ",";
    public static final String DELIM_START = "{";
    public static final String DELIM_END = "}";
    public static final String BRACKET_START = "[";
    public static final String BRACKET_END = "]";
    public static final String COLON = ":";
    public static final String AT = "@";
    public static final String HTML_NBSP = "&nbsp;";
    public static final String HTML_AMP = "&amp;";
    public static final String HTML_QUOTE = "&quot;";
    public static final String HTML_APOS = "&apos;";
    public static final String HTML_LT = "&lt;";
    public static final String HTML_GT = "&gt;";
    public static final String EMPTY_JSON = "{}";

    /**
     * 解析排序参数
     */
    public static String parseOrderBy(String sort) {
        if (StringUtils.isBlank(sort)) {
            return null;
        }
        String str = StringUtils.deleteWhitespace(sort);
        if (!str.matches(SQL_PATTERN)) {
            throw new IllegalArgumentException("Invalid sql: " + str + ", expected format: letter, number, underline, space, comma and decimal point!");
        }

        if (!StringUtils.containsAny(sort, "+", "-", ",")) {
            return sort;
        } else if (PATTERN_NONE_COMMA.matcher(str).matches()) { // e.g. a+b-c+
            return StringUtils.removeEnd(str.replaceAll("([\\w_])\\+", "$1 asc,").replaceAll("([\\w_])\\-", "$1 desc,"), ",");
        } else if (PATTERN_WITH_COMMA.matcher(str).matches()) { // e.g. +a,-b,+c
            return StringUtils.removeEnd(str.replaceAll("\\+([\\w_]*)(,|$)", "$1 asc,").replaceAll("\\-([\\w_]*)(,|$)", "$1 desc,"), ",");
        } else {
            throw new IllegalArgumentException("Invalid sort: " + sort + ", expected format: a+b-c+ or +a,-b,+c!");
        }
    }

    /**
     * 获取UUID字符串（去中划线）
     */
    public static String getUuidStr() {
        return StringUtils.removeAll(UUID.randomUUID().toString(), "-");
    }

    /**
     * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
     * 例如：
     *
     * <pre>
     * HelloWorld=》hello_world
     * Hello_World=》hello_world
     * HelloWorld_test=》hello_world_test
     * </pre>
     *
     * @param str 转换前的驼峰式命名的字符串，也可以为下划线形式
     * @return 转换后下划线方式命名的字符串
     */
    public static String toUnderlineCase(CharSequence str) {
        return toSymbolCase(str, '_');
    }

    /**
     * 将驼峰式命名的字符串转换为使用符号连接方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
     *
     * @param str    转换前的驼峰式命名的字符串，也可以为符号连接形式
     * @param symbol 连接符
     * @return 转换后符号连接方式命名的字符串
     * @since 4.0.10
     */
    public static String toSymbolCase(CharSequence str, char symbol) {
        if (str == null) {
            return null;
        }

        final int length = str.length();
        final StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < length; i++) {
            c = str.charAt(i);
            final Character preChar = (i > 0) ? str.charAt(i - 1) : null;
            if (Character.isUpperCase(c)) {
                // 遇到大写字母处理
                final Character nextChar = (i < str.length() - 1) ? str.charAt(i + 1) : null;
                if (null != preChar && Character.isUpperCase(preChar)) {
                    // 前一个字符为大写，则按照一个词对待，例如AB
                    sb.append(c);
                } else if (null != nextChar && (!Character.isLowerCase(nextChar))) {
                    // 后一个为非小写字母，按照一个词对待
                    if (null != preChar && symbol != preChar) {
                        // 前一个是非大写时按照新词对待，加连接符，例如xAB
                        sb.append(symbol);
                    }
                    sb.append(c);
                } else {
                    // 前后都为非大写按照新词对待
                    if (null != preChar && symbol != preChar) {
                        // 前一个非连接符，补充连接符
                        sb.append(symbol);
                    }
                    sb.append(Character.toLowerCase(c));
                }
            } else {
                if (symbol != c
                        && sb.length() > 0
                        && Character.isUpperCase(sb.charAt(sb.length() - 1))
                        && Character.isLowerCase(c)) {
                    // 当结果中前一个字母为大写，当前为小写(非数字或字符)，说明此字符为新词开始（连接符也表示新词）
                    sb.append(symbol);
                }
                // 小写或符号
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 首字母小写
     */
    public static String toUpperCaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母大写
     */
    public static String toLowerCaseFirstLetter(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}