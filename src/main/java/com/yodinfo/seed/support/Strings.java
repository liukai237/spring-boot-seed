package com.yodinfo.seed.support;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class Strings {

    public static String parseOrderBy(String sort) {
        if (StringUtils.isBlank(sort)) {
            return null;
        }

        String str = StringUtils.deleteWhitespace(sort);
        if (!StringUtils.containsAny(sort, "+", "-", ",")) {
            return sort;
        } else if (Pattern.compile("^([\\w_]*[\\+\\-])+$").matcher(str).matches()) { // e.g. a+b-c+
            return StringUtils.removeEnd(str.replaceAll("([\\w_])\\+", "$1 asc,").replaceAll("([\\w_])\\-", "$1 desc,"), ",");
        } else if (Pattern.compile("^([\\+\\-]+[\\w_]*,)+[\\+\\-][\\w_]*$").matcher(str).matches()) { // e.g. +a,-b,+c
            str += ",";
            return StringUtils.removeEnd(str.replaceAll("\\+([\\w_]*),", "$1 asc,").replaceAll("\\-([\\w_]*),", "$1 desc,"), ",");
        } else {
            throw new IllegalArgumentException("Invalid sort: " + sort + ", and format: a+b-c+ or +a,-b,+c!");
        }
    }
}