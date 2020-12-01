package com.iakuil.common.tool;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
@UtilityClass
public class Dates {

    public static final String ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private static String[] DEFAULT_PATTERNS = {ISO_PATTERN, "yyyy-MM-dd", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd M月d日HH:mm"};

    public static Date parseDate(String dateStr) {
        Date parsed;
        try {
            parsed = DateUtils.parseDate(dateStr, DEFAULT_PATTERNS);
        } catch (ParseException e) {
            throw new IllegalArgumentException("invalid date: " + dateStr);
        }

        return parsed;
    }

    public static Date getDayStart(String dateStr) {
        return getDayStart(parseDate(dateStr));
    }

    public static Date getDayStart(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        startOfDay(c);
        return c.getTime();
    }

    public static Date getDayEnd(String dateStr) {
        return getDayEnd(parseDate(dateStr));
    }

    public static Date getDayEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        endOfDay(c);
        return c.getTime();
    }

    public static Date getWeekStart(final String dateStr) {
        return getWeekStart(parseDate(dateStr));
    }

    public static Date getWeekStart(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, 1);
        startOfDay(c);
        return c.getTime();
    }

    public static Date getWeekEnd(String dateStr) {
        return getWeekEnd(parseDate(dateStr));
    }

    public static Date getWeekEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, 1);
        c.add(Calendar.DAY_OF_YEAR, 6);
        endOfDay(c);
        return c.getTime();
    }

    public static Date getMonthStart(final String dateStr) {
        return getMonthStart(parseDate(dateStr));
    }

    public static Date getMonthStart(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        startOfDay(c);
        return c.getTime();
    }

    public static Date getMonthEnd(String dateStr) {
        return getMonthEnd(parseDate(dateStr));
    }

    public static Date getMonthEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_YEAR, -1);
        endOfDay(c);
        return c.getTime();
    }

    public static Date getYearStart(final String dateStr) {
        return getYearStart(parseDate(dateStr));
    }

    public static Date getYearStart(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_YEAR, 1);
        startOfDay(c);
        return c.getTime();
    }

    public static Date getYearEnd(String dateStr) {
        return getYearEnd(parseDate(dateStr));
    }

    public static Date getYearEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_YEAR, 1);
        c.add(Calendar.DAY_OF_YEAR, -1);
        c.add(Calendar.YEAR, 1);
        endOfDay(c);
        return c.getTime();
    }

    private static void startOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void endOfDay(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
    }
}