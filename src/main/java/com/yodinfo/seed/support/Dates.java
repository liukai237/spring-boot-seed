package com.yodinfo.seed.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Dates {

    private static String[] DEFAULT_PATTERNS = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"};

    public static Date parseDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
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
        if (date == null) {
            return null;
        }
        return DateUtils.truncate(date, Calendar.DATE);
    }

    public static Date getDayEnd(String dateStr) {
        return getDayEnd(parseDate(dateStr));
    }

    public static Date getDayEnd(final Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }
}
