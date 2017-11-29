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
}
