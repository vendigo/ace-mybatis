package com.github.vendigo.acemybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;

public class AceMapperFactoryBean<T> implements FactoryBean<T> {
    private final AceProxyFactory<T> proxyFactory;
    private final SqlSessionFactory sqlSessionFactory;

    public AceMapperFactoryBean(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.proxyFactory = new AceProxyFactory<>(mapperInterface);
    }

    @Override
    public T getObject() throws Exception {
        return proxyFactory.newInstance(sqlSessionFactory);
    }

    @Override
    public Class<?> getObjectType() {
        return proxyFactory.getMapperType();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
