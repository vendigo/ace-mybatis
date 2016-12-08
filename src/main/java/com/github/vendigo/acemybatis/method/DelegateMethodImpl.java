package com.github.vendigo.acemybatis.method;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class DelegateMethodImpl implements AceMethod {
    private final MapperMethod mapperMethod;

    public DelegateMethodImpl(MapperMethod mapperMethod) {
        this.mapperMethod = mapperMethod;
    }

    @Override
    public Object execute(SqlSessionFactory sqlSessionFactory, Object[] args) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return mapperMethod.execute(sqlSession, args);
        }
    }
}
