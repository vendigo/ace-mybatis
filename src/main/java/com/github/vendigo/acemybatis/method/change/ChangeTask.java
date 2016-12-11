package com.github.vendigo.acemybatis.method.change;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.concurrent.Callable;

public class ChangeTask implements Callable<Integer> {

    private ChangeFunction changeFunction;
    private SqlSessionFactory sqlSessionFactory;
    private String statementName;
    private List<Object> entities;
    private Integer chunkSize;

    public ChangeTask(ChangeFunction changeFunction, SqlSessionFactory sqlSessionFactory, String statementName,
                      List<Object> entities, Integer chunkSize) {
        this.changeFunction = changeFunction;
        this.sqlSessionFactory = sqlSessionFactory;
        this.statementName = statementName;
        this.entities = entities;
        this.chunkSize = chunkSize;
    }

    @Override
    public Integer call() throws Exception {
        Integer changed = 0;

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            for (Object entity : entities) {
                changed += changeFunction.apply(sqlSession, statementName, entity);
                if (changed % chunkSize == 0) {
                    sqlSession.commit();
                }
            }
            sqlSession.commit();
        }
        return changed;
    }
}
