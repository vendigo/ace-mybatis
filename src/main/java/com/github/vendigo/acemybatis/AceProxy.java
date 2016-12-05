package com.github.vendigo.acemybatis;

import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AceProxy<T> implements InvocationHandler {
    private final SqlSessionFactory sqlSessionFactory;
    private final Class<T> mapperInterface;

    public AceProxy(SqlSessionFactory sqlSessionFactory, Class<T> mapperInterface) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                throw ExceptionUtil.unwrapThrowable(t);
            }
        }

        System.out.println("Called: "+method.getName());
        return null;
    }
}
