package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.DelegateMethodImpl;
import com.github.vendigo.acemybatis.method.select.StreamSelectMethodImpl;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class DeclarationParser {
    public static AceMethod parseMethodDeclaration(Class<?> mapperInterface, SqlSessionFactory sqlSessionFactory,
                                                   Method method) {
        Configuration config = sqlSessionFactory.getConfiguration();
        MapperMethod mapperMethod = new MapperMethod(mapperInterface, method, config);
        MapperMethod.SqlCommand command = new MapperMethod.SqlCommand(config, mapperInterface, method);
        MapperMethod.MethodSignature methodSignature = new MapperMethod.MethodSignature(config, mapperInterface, method);

        switch (command.getType()) {
            case SELECT:
                if (methodSignature.getReturnType().equals(Stream.class)) {
                    return new StreamSelectMethodImpl(method, methodSignature);
                }
                break;
        }

        return new DelegateMethodImpl(mapperMethod);
    }
}
