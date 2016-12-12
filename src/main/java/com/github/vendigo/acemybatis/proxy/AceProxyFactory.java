package com.github.vendigo.acemybatis.proxy;

import com.github.vendigo.acemybatis.config.AceConfig;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Proxy;

public class AceProxyFactory<T> {
    private final Class<T> mapperInterface;
    private final SqlSessionFactory sqlSessionFactory;
    private final AceConfig aceConfig;

    public AceProxyFactory(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory) {
        this.mapperInterface = mapperInterface;
        this.sqlSessionFactory = sqlSessionFactory;
        this.aceConfig = new AceConfig();
    }

    public AceProxyFactory(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory, AceConfig aceConfig) {
        this.mapperInterface = mapperInterface;
        this.sqlSessionFactory = sqlSessionFactory;
        this.aceConfig = aceConfig;
    }

    public Class<T> getMapperType() {
        return mapperInterface;
    }

    @SuppressWarnings("unchecked")
    public T newInstance() {
        AceProxy aceProxy = new AceProxy(sqlSessionFactory, mapperInterface, aceConfig);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, aceProxy);
    }
}
