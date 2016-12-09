package com.github.vendigo.acemybatis.method.select;

import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.MethodUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

public class SimpleStreamSelect implements AceMethod {
    private final Method method;
    private final MapperMethod.MethodSignature methodSignature;

    public SimpleStreamSelect(Method method, MapperMethod.MethodSignature methodSignature) {
        this.method = method;
        this.methodSignature = methodSignature;
    }

    @Override
    public Stream<Object> execute(SqlSessionFactory sqlSessionFactory, Object[] args) {
        String statementName = MethodUtils.getStatementName(method);
        Object param = methodSignature.convertArgsToSqlCommandParam(args);
        System.out.println("Params: "+param);

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<Object> list = sqlSession.selectList(statementName, param);
            return list.stream();
        }
    }
}
