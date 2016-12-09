package com.github.vendigo.acemybatis.method;

import java.lang.reflect.Method;

public class CommonUtils {
    public static String getStatementName(Method method) {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    public static String getCountStatementName(Method method) {
        return getStatementName(method)+"Count";
    }

    public static int computeThreadPullSize(int declaredValue, int entriesCount, int chunkSize) {
        if (declaredValue > 0 ) {
            return declaredValue;
        }
        int maxCores = Runtime.getRuntime().availableProcessors();
        int chunksCount = entriesCount / chunkSize;
        return Math.min(Math.max(chunksCount, 1), maxCores);
    }
}
