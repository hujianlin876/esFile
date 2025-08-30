package com.esfile.common.util;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 日期工具类
 */
public class DateUtil {
    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }
}
