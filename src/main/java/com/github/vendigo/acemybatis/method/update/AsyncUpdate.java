package com.github.vendigo.acemybatis.method.update;

import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.change.ChangeHelper;
import com.github.vendigo.acemybatis.method.CommonUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.List;

public class AsyncUpdate implements AceMethod {

    private Method method;
    private MapperMethod.MethodSignature methodSignature;
    private int threadCount;
    private int chunkSize;

    public AsyncUpdate(Method method, MapperMethod.MethodSignature methodSignature, int chunkSize, int threadCount) {
        this.method = method;
        this.methodSignature = methodSignature;
        this.threadCount = threadCount;
        this.chunkSize = chunkSize;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        String statementName = CommonUtils.getStatementName(method);
        List<Object> entities = (List<Object>) methodSignature.convertArgsToSqlCommandParam(args);
        int computedThreadCount = CommonUtils.computeThreadPullSize(threadCount, entities.size(), chunkSize);
        return ChangeHelper.applyAsync(SqlSession::update, sqlSessionFactory, statementName, entities, chunkSize,
                computedThreadCount);
    }
}
