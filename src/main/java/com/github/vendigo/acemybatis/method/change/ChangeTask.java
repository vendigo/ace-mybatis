package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.parser.ParamsHolder;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.concurrent.Callable;

/**
 * Task for inserting/updating/deleting bunch of entities in chunks.
 */
public class ChangeTask implements Callable<Integer> {
    private AceConfig config;
    private ChangeFunction changeFunction;
    private SqlSessionFactory sqlSessionFactory;
    private String statementName;
    private ParamsHolder params;

    ChangeTask(AceConfig config, ChangeFunction changeFunction, SqlSessionFactory sqlSessionFactory, String statementName,
               ParamsHolder params) {
        this.config = config;
        this.changeFunction = changeFunction;
        this.sqlSessionFactory = sqlSessionFactory;
        this.statementName = statementName;
        this.params = params;
    }

    @Override
    public Integer call() throws Exception {
        return ChangeHelper.applySingleCore(config, changeFunction, sqlSessionFactory, statementName, params);
    }
}
