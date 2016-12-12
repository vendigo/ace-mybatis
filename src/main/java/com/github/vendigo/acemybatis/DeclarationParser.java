package com.github.vendigo.acemybatis;

import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.CommonUtils;
import com.github.vendigo.acemybatis.method.DelegateMethodImpl;
import com.github.vendigo.acemybatis.method.delete.AsyncDelete;
import com.github.vendigo.acemybatis.method.delete.SyncDelete;
import com.github.vendigo.acemybatis.method.insert.AsyncInsert;
import com.github.vendigo.acemybatis.method.insert.SyncInsert;
import com.github.vendigo.acemybatis.method.select.ReactiveStreamSelect;
import com.github.vendigo.acemybatis.method.select.SimpleStreamSelect;
import com.github.vendigo.acemybatis.method.update.AsyncUpdate;
import com.github.vendigo.acemybatis.method.update.SyncUpdate;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class DeclarationParser {
    private static final Logger log = LoggerFactory.getLogger(DeclarationParser.class);

    public static AceMethod parseMethodDeclaration(Class<?> mapperInterface, SqlSessionFactory sqlSessionFactory,
                                                   Method method) {
        Configuration config = sqlSessionFactory.getConfiguration();
        MapperMethod mapperMethod = new MapperMethod(mapperInterface, method, config);
        MapperMethod.SqlCommand command = new MapperMethod.SqlCommand(config, mapperInterface, method);
        MapperMethod.MethodSignature methodSignature = new MapperMethod.MethodSignature(config, mapperInterface, method);

        switch (command.getType()) {
            case SELECT:
                if (methodSignature.getReturnType().equals(Stream.class)) {
                    if (config.hasStatement(CommonUtils.getCountStatementName(method))) {
                        log.info("Using reactive stream select for {}", method.getName());
                        return new ReactiveStreamSelect(method, methodSignature, 1000, 0);
                    } else {
                        log.info("Using simple stream select for {}", method.getName());
                        return new SimpleStreamSelect(method, methodSignature);
                    }
                }
                break;
            case INSERT:
                if (methodSignature.getReturnType().equals(CompletableFuture.class)) {
                    log.info("Using async insert for {}", method.getName());
                    return new AsyncInsert(method, methodSignature, 1000, 0);
                } else {
                    List<Class<?>> parameterTypes = Arrays.asList(method.getParameterTypes());
                    if (parameterTypes.size() == 1 && parameterTypes.get(0).equals(List.class)) {
                        log.info("Using sync insert for {}", method.getName());
                        return new SyncInsert(method, methodSignature, 1000, 0);
                    }
                }
                break;
            case UPDATE:
                if (methodSignature.getReturnType().equals(CompletableFuture.class)) {
                    log.info("Using async update for {}", method.getName());
                    return new AsyncUpdate(method, methodSignature, 1000, 0);
                } else {
                    List<Class<?>> parameterTypes = Arrays.asList(method.getParameterTypes());
                    if (parameterTypes.size() == 1 && parameterTypes.get(0).equals(List.class)) {
                        log.info("Using sync update for {}", method.getName());
                        return new SyncUpdate(method, methodSignature, 1000, 0);
                    }
                }
                break;
            case DELETE:
                if (methodSignature.getReturnType().equals(CompletableFuture.class)) {
                    log.info("Using async delete for {}", method.getName());
                    return new AsyncDelete(method, methodSignature, 1000, 0);
                } else {
                    List<Class<?>> parameterTypes = Arrays.asList(method.getParameterTypes());
                    if (parameterTypes.size() == 1 && parameterTypes.get(0).equals(List.class)) {
                        log.info("Using sync delete for {}", method.getName());
                        return new SyncDelete(method, methodSignature, 1000, 0);
                    }
                }
                break;
        }

        log.info("Delegating query for {}", method.getName());
        return new DelegateMethodImpl(mapperMethod);
    }
}
