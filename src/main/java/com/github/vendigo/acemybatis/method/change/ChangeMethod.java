package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.parser.ParamsHolder;
import com.github.vendigo.acemybatis.parser.ParamsParser;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSessionFactory;

import static com.github.vendigo.acemybatis.utils.Validator.notNull;

/**
 * Implementation for change methods (insert/update/delete).
 */
public class ChangeMethod implements AceMethod {
    private final String statementName;
    private final boolean async;
    private final MapperMethod.MethodSignature methodSignature;
    private final AceConfig config;
    private final ChangeFunction changeFunction;

    public ChangeMethod(String statementName, boolean async, MapperMethod.MethodSignature methodSignature,
                        AceConfig config, ChangeFunction changeFunction) {
        this.methodSignature = notNull(methodSignature);
        this.async = async;
        this.statementName = notNull(statementName);
        this.config = notNull(config);
        this.changeFunction = notNull(changeFunction);
    }

    @Override
    public Integer execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        ParamsHolder params = ParamsParser.parseParams(config, methodSignature, args);

        if (async && enoughForAsync(params, config)) {
            return ChangeHelper.applyInParallel(config, changeFunction, sqlSessionFactory, statementName, params);
        } else {
            return ChangeHelper.applySingleCore(config, changeFunction, sqlSessionFactory, statementName, params);
        }
    }

    private boolean enoughForAsync(ParamsHolder params, AceConfig config) {
        return params.getEntities().size() > 2 * config.getUpdateChunkSize();
    }

}
