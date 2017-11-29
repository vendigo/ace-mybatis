package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.CommonUtils;
import com.github.vendigo.acemybatis.parser.ParamsHolder;
import com.github.vendigo.acemybatis.parser.ParamsParser;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.concurrent.CompletableFuture;

import static com.github.vendigo.acemybatis.utils.Validator.notNull;

/**
 * Generic implementation for change methods (insert/update/delete).
 */
public abstract class ChangeMethod implements AceMethod {
    private final MapperMethod.MethodSignature methodSignature;
    private final AceConfig config;
    private final String statementName;
    private final ChangeFunction changeFunction;

    public ChangeMethod(String statementName, MapperMethod.MethodSignature methodSignature, AceConfig config,
                        ChangeFunction changeFunction) {
        this.methodSignature = notNull(methodSignature);
        this.statementName = notNull(statementName);
        this.config = notNull(config);
        this.changeFunction = notNull(changeFunction);
    }

    @SuppressWarnings("unchecked")
    protected CompletableFuture<Integer> doExecute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        ParamsHolder params = ParamsParser.parseParams(config, methodSignature, args);
        int threadCount = CommonUtils.computeThreadPullSize(config.getThreadCount(), params.getEntities().size(),
                config.getUpdateChunkSize());
        config.setThreadCount(threadCount);
        return ChangeHelper.applyAsync(config, changeFunction, sqlSessionFactory, statementName, params);
    }

}
