package com.github.vendigo.acemybatis.method;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * Generic interface for all ace method implementations.
 */
public interface AceMethod {
    Object execute(SqlSessionFactory sqlSessionFactory, Object[] args) throws Exception;
}
