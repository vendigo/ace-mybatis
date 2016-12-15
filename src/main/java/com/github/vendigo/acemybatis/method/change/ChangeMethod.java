package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.CommonUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Generic implementation for change methods (insert/update/delete).
 */
public abstract class ChangeMethod implements AceMethod {
    private MapperMethod.MethodSignature methodSignature;
    private AceConfig config;
    private String statementName;
    private ChangeFunction changeFunction;

    public ChangeMethod(Method method, MapperMethod.MethodSignature methodSignature, AceConfig config,
                        ChangeFunction changeFunction) {
        this.methodSignature = methodSignature;
        this.statementName = CommonUtils.getStatementName(method);
        this.config = config;
        this.changeFunction = changeFunction;
    }

    @SuppressWarnings("unchecked")
    protected CompletableFuture<Integer> doExecute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        List<Object> entities = (List<Object>) methodSignature.convertArgsToSqlCommandParam(args);
        int threadCount = CommonUtils.computeThreadPullSize(config.getThreadCount(), entities.size(),
                config.getUpdateChunkSize());
        return ChangeHelper.applyAsync(changeFunction, sqlSessionFactory, statementName, entities,
                config.getUpdateChunkSize(),
                threadCount);
    }

}
