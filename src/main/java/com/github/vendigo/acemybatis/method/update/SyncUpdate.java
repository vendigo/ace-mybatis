package com.github.vendigo.acemybatis.method.update;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.change.ChangeMethod;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;

public class SyncUpdate extends ChangeMethod {

    public SyncUpdate(Method method, MapperMethod.MethodSignature methodSignature, AceConfig config) {
        super(method, methodSignature, config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        return doExecute(SqlSession::update, sqlSessionFactory, args).get();
    }
}
