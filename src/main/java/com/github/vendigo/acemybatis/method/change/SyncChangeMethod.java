package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;

public class SyncChangeMethod extends ChangeMethod {

    public SyncChangeMethod(Method method, MapperMethod.MethodSignature methodSignature, AceConfig config,
                            ChangeFunction changeFunction) {
        super(method, methodSignature, config, changeFunction);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        return doExecute(sqlSessionFactory, args).get();
    }
}
