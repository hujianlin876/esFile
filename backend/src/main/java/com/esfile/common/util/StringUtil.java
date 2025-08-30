package com.esfile.common.util;
/**
 * 字符串工具类
 */
public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
