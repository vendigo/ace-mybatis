package com.github.vendigo.acemybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;

class AceMapperFactoryBean<T> implements FactoryBean<T> {
    private final AceMapperFactory<T> proxyFactory;

    AceMapperFactoryBean(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory, AceConfig aceConfig) {
        this.proxyFactory = new AceMapperFactory<>(mapperInterface, sqlSessionFactory, aceConfig);
    }

    @Override
    public T getObject() throws Exception {
        return proxyFactory.create();
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
