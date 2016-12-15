package com.github.vendigo.acemybatis.parser;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.CommonUtils;
import com.github.vendigo.acemybatis.method.DelegateMethodImpl;
import com.github.vendigo.acemybatis.method.change.*;
import com.github.vendigo.acemybatis.method.select.ReactiveStreamSelect;
import com.github.vendigo.acemybatis.method.select.SimpleStreamSelect;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Parses method declarations and choose ace method implementation.
 * Rules
 * <ul>
 *     <li>Return type ChangeCollector - {@link CollectorMethod}</li>
 *     <li>Select statement with countQuery - {@link ReactiveStreamSelect}</li>
 *     <li>Insert/Update/Delete with return type Completable future - {@link AsyncChangeMethod}</li>
 *     <li>Insert/Update/Delete with return type int - {@link SyncChangeMethod}</li>
 *     <li>Otherwise - {@link DelegateMethodImpl}</li>
 * </ul>
 */
public class DeclarationParser {
    private static final Logger log = LoggerFactory.getLogger(DeclarationParser.class);

    public static AceMethod parseMethodDeclaration(AceConfig aceConfig, Class<?> mapperInterface,
                                                   SqlSessionFactory sqlSessionFactory, Method method) {
        Configuration config = sqlSessionFactory.getConfiguration();
        MapperMethod mapperMethod = new MapperMethod(mapperInterface, method, config);
        MapperMethod.SqlCommand command = new MapperMethod.SqlCommand(config, mapperInterface, method);
        MapperMethod.MethodSignature methodSignature = new MapperMethod.MethodSignature(config, mapperInterface, method);
        Optional<AceMethod> parsedMethod;

        if (methodSignature.getReturnType().equals(ChangeCollector.class)) {
            return new CollectorMethod(resolveChangeFunction(command.getType()), method, aceConfig);
        }

        if (command.getType() == SqlCommandType.SELECT) {
            parsedMethod = parseSelect(methodSignature, config, method, aceConfig);
        } else {
            parsedMethod = parseChange(methodSignature, method, aceConfig, command.getType());
        }

        return parsedMethod.orElse(new DelegateMethodImpl(mapperMethod));
    }

    private static ChangeFunction resolveChangeFunction(SqlCommandType type) {
        switch (type) {
            case INSERT:
                return SqlSession::insert;
            case UPDATE:
                return SqlSession::update;
            case DELETE:
                return SqlSession::delete;
        }

        throw new IllegalArgumentException(type + " is not supported for change collector");
    }

    private static Optional<AceMethod> parseSelect(MapperMethod.MethodSignature methodSignature, Configuration config,
                                                   Method method, AceConfig aceConfig) {
        if (methodSignature.getReturnType().equals(Stream.class)) {
            if (config.hasStatement(CommonUtils.getCountStatementName(method))) {
                log.info("Using reactive stream select for {}", method.getName());
                return Optional.of(new ReactiveStreamSelect(method, methodSignature, aceConfig));
            } else {
                log.info("Using simple stream select for {}", method.getName());
                return Optional.of(new SimpleStreamSelect(method, methodSignature));
            }
        }
        return Optional.empty();
    }

    private static Optional<AceMethod> parseChange(MapperMethod.MethodSignature methodSignature,
                                                   Method method, AceConfig aceConfig, SqlCommandType commandType) {
        if (methodSignature.getReturnType().equals(CompletableFuture.class)) {
            log.info("Using async version for {}", method.getName());
            return Optional.of(new AsyncChangeMethod(method, methodSignature, aceConfig, resolveChangeFunction(commandType)));
        } else {
            List<Class<?>> parameterTypes = Arrays.asList(method.getParameterTypes());
            if (parameterTypes.size() == 1 && parameterTypes.get(0).equals(List.class)) {
                log.info("Using sync version for {}", method.getName());
                return Optional.of(new SyncChangeMethod(method, methodSignature, aceConfig, resolveChangeFunction(commandType)));
            }
        }
        return Optional.empty();
    }
}
