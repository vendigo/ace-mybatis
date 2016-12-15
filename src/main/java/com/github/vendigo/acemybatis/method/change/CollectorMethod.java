package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.CommonUtils;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;

public class CollectorMethod implements AceMethod {
    private ChangeFunction changeFunction;
    private AceConfig config;
    private String statementName;

    public CollectorMethod(ChangeFunction changeFunction, Method method, AceConfig config) {
        this.changeFunction = changeFunction;
        this.statementName = CommonUtils.getStatementName(method);
        this.config = config;
    }

    @Override
    public SimpleChangeCollector execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        return new SimpleChangeCollector(config, sqlSessionFactory, statementName, changeFunction);
    }
}
