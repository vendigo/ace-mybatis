package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.CommonUtils;
import com.github.vendigo.acemybatis.parser.ParamsHolder;
import com.github.vendigo.acemybatis.parser.ParamsParser;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
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
        ParamsHolder params = ParamsParser.parseParams(methodSignature, args);
        int threadCount = CommonUtils.computeThreadPullSize(config.getThreadCount(), params.getEntities().size(),
                config.getUpdateChunkSize());
        return ChangeHelper.applyAsync(changeFunction, sqlSessionFactory, statementName, params,
                config.getUpdateChunkSize(),
                threadCount);
    }

}
