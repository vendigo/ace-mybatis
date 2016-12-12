package com.github.vendigo.acemybatis.method.update;

import com.github.vendigo.acemybatis.method.change.ChangeMethod;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class AsyncUpdate extends ChangeMethod {

    public AsyncUpdate(Method method, MapperMethod.MethodSignature methodSignature, int chunkSize, int threadCount) {
        super(method, methodSignature, chunkSize, threadCount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<Integer> execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        return doExecute(SqlSession::update, sqlSessionFactory, args);
    }
}
