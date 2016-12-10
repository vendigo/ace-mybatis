package com.github.vendigo.acemybatis.method.insert;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.concurrent.Callable;

public class InsertTask implements Callable<Integer> {

    private SqlSessionFactory sqlSessionFactory;
    private String statementName;
    private List<Object> entities;
    private Integer chunkSize;

    public InsertTask(SqlSessionFactory sqlSessionFactory, String statementName, List<Object> entities, Integer chunkSize) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.statementName = statementName;
        this.entities = entities;
        this.chunkSize = chunkSize;
    }

    @Override
    public Integer call() throws Exception {
        Integer inserted = 0;

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            for (Object entity : entities) {
                inserted += sqlSession.insert(statementName, entity);
                if (inserted % chunkSize == 0) {
                    sqlSession.commit();
                }
            }
            sqlSession.commit();
        }
        return inserted;
    }
}
