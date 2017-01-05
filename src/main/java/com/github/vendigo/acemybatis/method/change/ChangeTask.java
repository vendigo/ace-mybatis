package com.github.vendigo.acemybatis.method.change;

import com.github.vendigo.acemybatis.parser.ParamsHolder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.concurrent.Callable;

/**
 * Task for inserting/updating/deleting bunch of entities in chunks.
 */
public class ChangeTask implements Callable<Integer> {

    private ChangeFunction changeFunction;
    private SqlSessionFactory sqlSessionFactory;
    private String statementName;
    private ParamsHolder params;
    private Integer chunkSize;

    public ChangeTask(ChangeFunction changeFunction, SqlSessionFactory sqlSessionFactory, String statementName,
                      ParamsHolder params, Integer chunkSize) {
        this.changeFunction = changeFunction;
        this.sqlSessionFactory = sqlSessionFactory;
        this.statementName = statementName;
        this.params = params;
        this.chunkSize = chunkSize;
    }

    @Override
    public Integer call() throws Exception {
        Integer changed = 0;

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            for (Object entity : params.getEntities()) {
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
