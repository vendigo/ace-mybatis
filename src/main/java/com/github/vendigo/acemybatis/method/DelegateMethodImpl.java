package com.github.vendigo.acemybatis.method;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * Ace method implementation which delegates to default myBatis implementations
 * (e.g. select list, insert/update/delete one entry, etc.).
 */
public class DelegateMethodImpl implements AceMethod {
    private final MapperMethod mapperMethod;

    public DelegateMethodImpl(MapperMethod mapperMethod) {
        this.mapperMethod = mapperMethod;
    }

    @Override
    public Object execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return mapperMethod.execute(sqlSession, args);
        }
    }
}
