package com.github.vendigo.acemybatis.method;

import java.lang.reflect.Method;

public class MethodUtils {
    public static String getStatementName(Method method) {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    public static String getCountStatementName(Method method) {
        return getStatementName(method)+"Count";
    }
}
