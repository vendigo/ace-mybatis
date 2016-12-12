package com.github.vendigo.acemybatis.config;

import com.github.vendigo.acemybatis.proxy.AceProxyFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;

public class AceMapperFactoryBean<T> implements FactoryBean<T> {
    private final AceProxyFactory<T> proxyFactory;

    public AceMapperFactoryBean(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory) {
        this.proxyFactory = new AceProxyFactory<>(mapperInterface, sqlSessionFactory);
    }

    @Override
    public T getObject() throws Exception {
        return proxyFactory.newInstance();
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
