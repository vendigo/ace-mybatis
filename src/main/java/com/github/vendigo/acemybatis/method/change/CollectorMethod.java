package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.AceMethod;
import org.apache.ibatis.session.SqlSessionFactory;

import static com.github.vendigo.acemybatis.utils.Validator.notNull;

public class CollectorMethod implements AceMethod {
    private final ChangeFunction changeFunction;
    private final AceConfig config;
    private final String statementName;

    public CollectorMethod(ChangeFunction changeFunction, String statementName, AceConfig config) {
        this.changeFunction = notNull(changeFunction);
        this.statementName = notNull(statementName);
        this.config = notNull(config);
    }

    @Override
    public SimpleChangeCollector execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        return new SimpleChangeCollector(config, sqlSessionFactory, statementName, changeFunction);
    }
}
