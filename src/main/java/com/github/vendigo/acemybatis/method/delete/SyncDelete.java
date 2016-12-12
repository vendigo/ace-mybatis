package com.github.vendigo.acemybatis.method.delete;

import com.github.vendigo.acemybatis.method.change.ChangeMethod;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;

public class SyncDelete extends ChangeMethod {

    public SyncDelete(Method method, MapperMethod.MethodSignature methodSignature, int chunkSize, int threadCount) {
        super(method, methodSignature, chunkSize, threadCount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        return doExecute(SqlSession::delete, sqlSessionFactory, args).get();
    }
}
