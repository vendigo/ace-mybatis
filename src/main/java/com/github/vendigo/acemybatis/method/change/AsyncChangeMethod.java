package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Generic method for async batch insert/update/delete.
 */
public class AsyncChangeMethod extends ChangeMethod {

    public AsyncChangeMethod(String statementName, MapperMethod.MethodSignature methodSignature, AceConfig config,
                             ChangeFunction changeFunction) {
        super(statementName, methodSignature, config, changeFunction);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<Integer> execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        return doExecute(sqlSessionFactory, args);
    }
}
