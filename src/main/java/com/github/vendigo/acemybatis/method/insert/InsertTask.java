package com.github.vendigo.acemybatis.method.insert;

import com.github.vendigo.acemybatis.method.CommonUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

public class InsertTask implements Callable<Integer> {

    private SqlSessionFactory sqlSessionFactory;
    private Method method;
    private List<Object> entities;
    private Integer chunkSize;

    public InsertTask(SqlSessionFactory sqlSessionFactory, Method method, List<Object> entities, Integer chunkSize) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.method = method;
        this.entities = entities;
        this.chunkSize = chunkSize;
    }

    @Override
    public Integer call() throws Exception {
        String statementName = CommonUtils.getStatementName(method);
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
