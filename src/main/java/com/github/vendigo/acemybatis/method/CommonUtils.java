package com.github.vendigo.acemybatis.method;

import java.lang.reflect.Method;

public class CommonUtils {
    /**
     * Interface class is passed explicitly instead of method.getDeclaringClass
     * to allow inheritance in mapper interfaces.
     */
    public static String getStatementName(Class interfaceClass, Method method) {
        return interfaceClass.getName() + "." + method.getName();
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
