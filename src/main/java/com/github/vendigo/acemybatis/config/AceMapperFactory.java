package com.github.vendigo.acemybatis.config;

import com.github.vendigo.acemybatis.proxy.AceProxy;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Proxy;

import static com.github.vendigo.acemybatis.config.AceConfigResolver.resolveConfig;
import static com.github.vendigo.acemybatis.utils.Validator.notNull;

/**
 * Can be used for explicit creation ace mappers. Use inner builder for convenient configuring.
 * For automatic discovering/generating mappers see {@link AceMapperScannerConfigurer}.
 * @param <T> - Class of mapper interface.
 */
public class AceMapperFactory<T> {
    private final Class<T> mapperInterface;
    private final SqlSessionFactory sqlSessionFactory;
    private final AceConfig aceConfig;

    public AceMapperFactory(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory, AceConfig aceConfig) {
        this.mapperInterface = notNull(mapperInterface);
        this.sqlSessionFactory = notNull(sqlSessionFactory);
        this.aceConfig = notNull(aceConfig);
    }

    @SuppressWarnings("unchecked")
    public T create() {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface},
                new AceProxy(sqlSessionFactory, mapperInterface, aceConfig));
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private Class<T> mapperInterface;
        private SqlSessionFactory sqlSessionFactory;
        private AceConfig config;
        private int selectChunkSize;
        private int updateChunkSize;
        private int threadCount;

        public Builder<T> mapperInterface(Class<T> mapperInterface) {
            this.mapperInterface = mapperInterface;
            return this;
        }

        public Builder<T> sqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
            this.sqlSessionFactory = sqlSessionFactory;
            return this;
        }

        public Builder<T> config(AceConfig config) {
            this.config = config;
            return this;
        }

        public Builder<T> selectChunkSize(int selectChunkSize) {
            this.selectChunkSize = selectChunkSize;
            return this;
        }

        public Builder<T> updateChunkSize(int updateChunkSize) {
            this.updateChunkSize = updateChunkSize;
            return this;
        }

        public Builder<T> threadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }

        public T build() {
            return new AceMapperFactory<>(mapperInterface, sqlSessionFactory, resolveConfig(config,
                    selectChunkSize, updateChunkSize, threadCount)).create();
        }
    }
}
