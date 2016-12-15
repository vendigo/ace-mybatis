package com.github.vendigo.acemybatis.proxy;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.parser.DeclarationParser;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InvocationHandler for generated ace mappers.
 * @param <T> - type of mapper interface.
 */
public class AceProxy<T> implements InvocationHandler, Serializable {
    private final SqlSessionFactory sqlSessionFactory;
    private final Class<T> mapperInterface;
    private final AceConfig aceConfig;
    private final Map<Method, AceMethod> cachedMethods = new ConcurrentHashMap<>();

    public AceProxy(SqlSessionFactory sqlSessionFactory, Class<T> mapperInterface, AceConfig aceConfig) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.mapperInterface = mapperInterface;
        this.aceConfig = aceConfig;
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

        return getOrCreate(method).execute(sqlSessionFactory, args);
    }

    private AceMethod getOrCreate(Method method) {
        if (!cachedMethods.containsKey(method)) {
            cachedMethods.put(method, DeclarationParser.parseMethodDeclaration(aceConfig, mapperInterface,
                    sqlSessionFactory, method));
        }
        return cachedMethods.get(method);
    }
}
