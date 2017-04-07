package com.by_syk.schttableserver.util;

/**
 * 字符串操作工具类
 */
public class StringUtil {
    /**
     * 空字符串判断
     * 
     * @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }
}
