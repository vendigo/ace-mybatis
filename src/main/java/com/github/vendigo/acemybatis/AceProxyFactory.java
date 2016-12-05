package com.github.vendigo.acemybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Proxy;

public class AceProxyFactory<T> {
    private final Class<T> mapperInterface;

    public AceProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperType() {
        return mapperInterface;
    }

    @SuppressWarnings("unchecked")
    public T newInstance(SqlSessionFactory sqlSessionFactory) {
        AceProxy aceProxy = new AceProxy(sqlSessionFactory, mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, aceProxy);
    }
}
