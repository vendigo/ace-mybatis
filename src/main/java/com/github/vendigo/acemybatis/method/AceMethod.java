package com.github.vendigo.acemybatis.method;

import org.apache.ibatis.session.SqlSessionFactory;

public interface AceMethod {
    Object execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception;
}
