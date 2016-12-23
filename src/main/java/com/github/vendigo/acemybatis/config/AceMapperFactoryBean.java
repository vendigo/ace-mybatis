package com.github.vendigo.acemybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Creates ace mappers. Is used by {@link AceMapperScannerConfigurer}.
 * @param <T>
 */
class AceMapperFactoryBean<T> implements FactoryBean<T> {
    private SqlSessionFactory sqlSessionFactory;
    private Class<T> mapperInterface;
    private AceConfig aceConfig;

    AceMapperFactoryBean(Class<T> mapperInterface, AceConfig aceConfig) {
        this.mapperInterface = mapperInterface;
        this.aceConfig = aceConfig;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public T getObject() throws Exception {
        return new AceMapperFactory<>(mapperInterface, sqlSessionFactory, aceConfig).create();
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
