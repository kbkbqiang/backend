package com.zq.backend.common.utils;


import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: StringUtil 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:51:49
 */
public final class StringUtil {

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}