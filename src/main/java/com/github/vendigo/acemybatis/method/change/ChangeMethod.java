package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.CommonUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class ChangeMethod implements AceMethod {
    private MapperMethod.MethodSignature methodSignature;
    private int threadCount;
    private int chunkSize;
    private String statementName;

    public ChangeMethod(Method method, MapperMethod.MethodSignature methodSignature, int chunkSize, int threadCount) {
        this.methodSignature = methodSignature;
        this.threadCount = threadCount;
        this.chunkSize = chunkSize;
        this.statementName = CommonUtils.getStatementName(method);
    }

    @SuppressWarnings("unchecked")
    protected CompletableFuture<Integer> doExecute(ChangeFunction changeFunction, SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        List<Object> entities = (List<Object>) methodSignature.convertArgsToSqlCommandParam(args);
        int threadCount = CommonUtils.computeThreadPullSize(this.threadCount, entities.size(), chunkSize);
        return ChangeHelper.applyAsync(changeFunction, sqlSessionFactory, statementName, entities, chunkSize,
                threadCount);
    }

}
