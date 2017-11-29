package com.github.vendigo.acemybatis.method.change;

import org.apache.ibatis.session.SqlSession;

/**
 * SqlSession method. (insert/update/delete).
 */
@FunctionalInterface
public interface ChangeFunction {
    void apply(SqlSession sqlSession, String statementName, Object entity);
}
