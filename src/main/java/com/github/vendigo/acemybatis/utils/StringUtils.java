package com.github.vendigo.acemybatis.utils;

/**
 * Created to reduce the number of dependencies (e.g. apache-commons)
 */
public class StringUtils {
    private StringUtils() {
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if(cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }
}
