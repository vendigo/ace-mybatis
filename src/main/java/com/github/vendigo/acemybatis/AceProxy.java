package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.method.AceMethod;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AceProxy<T> implements InvocationHandler, Serializable {
    private final SqlSessionFactory sqlSessionFactory;
    private final Class<T> mapperInterface;
    private final Map<Method, AceMethod> cachedMethods = new ConcurrentHashMap<>();

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

        return getOrCreate(mapperInterface, sqlSessionFactory, method).execute(sqlSessionFactory, args);
    }

    private AceMethod getOrCreate(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory,
                                  Method method) {
        if (!cachedMethods.containsKey(method)) {
            cachedMethods.put(method, DeclarationParser.parseMethodDeclaration(mapperInterface, sqlSessionFactory,
                    method));
        }
        return cachedMethods.get(method);
    }
}
