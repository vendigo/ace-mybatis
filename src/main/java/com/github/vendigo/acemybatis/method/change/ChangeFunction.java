package com.github.vendigo.acemybatis.method.change;

import org.apache.ibatis.session.SqlSession;

@FunctionalInterface
public interface ChangeFunction {
    int apply(SqlSession sqlSession, String statementName, Object entity);
}
