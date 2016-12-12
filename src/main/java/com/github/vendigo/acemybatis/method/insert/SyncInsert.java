package com.github.vendigo.acemybatis.method.insert;

import com.github.vendigo.acemybatis.method.change.ChangeMethod;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;

public class SyncInsert extends ChangeMethod {

    public SyncInsert(Method method, MapperMethod.MethodSignature methodSignature, int chunkSize, int threadCount) {
        super(method, methodSignature, chunkSize, threadCount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        return doExecute(SqlSession::insert, sqlSessionFactory, args).get();
    }
}
