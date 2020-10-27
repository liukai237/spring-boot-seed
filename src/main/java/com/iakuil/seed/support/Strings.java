package com.iakuil.seed.support;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class Strings {

    private static final Pattern PATTERN_NONE_COMMA = Pattern.compile("^([\\w_]+[\\+\\-]{1})+$");
    private static final Pattern PATTERN_WITH_COMMA = Pattern.compile("^([\\+\\-]+[\\w_]*,)+[\\+\\-][\\w_]*$");

    public static String parseOrderBy(String sort) {
        if (StringUtils.isBlank(sort)) {
            return null;
        }
        String str = StringUtils.deleteWhitespace(sort);
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

    public static String getUuidStr() {
        return StringUtils.removeAll(UUID.randomUUID().toString(), "-");
    }
}