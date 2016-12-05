package com.github.vendigo.acemybatis;

import org.apache.ibatis.session.SqlSessionFactory;

public interface AceMethod<R> {
    R execute(SqlSessionFactory sqlSessionFactory, Object[] args);
}
