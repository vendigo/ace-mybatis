package com.github.vendigo.acemybatis.parser;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.config.Async;
import com.github.vendigo.acemybatis.config.NonBatchMethod;
import com.github.vendigo.acemybatis.method.AceMethod;
import com.github.vendigo.acemybatis.method.DelegateMethodImpl;
import com.github.vendigo.acemybatis.method.change.ChangeCollector;
import com.github.vendigo.acemybatis.method.change.ChangeFunction;
import com.github.vendigo.acemybatis.method.change.ChangeMethod;
import com.github.vendigo.acemybatis.method.change.CollectorMethod;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * Parses method declarations and choose ace method implementation.
 * Rules
 * <ul>
 * <li>Return type ChangeCollector - {@link CollectorMethod}</li>
 * <li>Insert/Update/Delete with return type int/void - {@link ChangeMethod}</li>
 * <li>Otherwise - {@link DelegateMethodImpl}</li>
 * </ul>
 */
public class DeclarationParser {
    private static final EnumSet<SqlCommandType> CHANGE_COMMANDS = EnumSet.of(SqlCommandType.INSERT, SqlCommandType.UPDATE,
            SqlCommandType.DELETE);

    public static AceMethod parseMethodDeclaration(AceConfig aceConfig, Class<?> mapperInterface,
                                                   SqlSessionFactory sqlSessionFactory, Method method) {
        Configuration config = sqlSessionFactory.getConfiguration();
        MapperMethod mapperMethod = new MapperMethod(mapperInterface, method, config);
        MapperMethod.SqlCommand command = new MapperMethod.SqlCommand(config, mapperInterface, method);
        MapperMethod.MethodSignature methodSignature = new MapperMethod.MethodSignature(config, mapperInterface, method);
        String statementName = mapperInterface.getName() + "." + method.getName();
        Optional<AceMethod> parsedMethod = Optional.empty();

        if (CHANGE_COMMANDS.contains(command.getType()) && methodSignature.getReturnType().equals(ChangeCollector.class)) {
            return new CollectorMethod(resolveChangeFunction(command.getType()), statementName, aceConfig);
        }

        if (CHANGE_COMMANDS.contains(command.getType())) {
            parsedMethod = parseChange(methodSignature, method, statementName, aceConfig, command.getType());
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

    private static Optional<AceMethod> parseChange(MapperMethod.MethodSignature methodSignature,
                                                   Method method, String statementName, AceConfig aceConfig,
                                                   SqlCommandType commandType) {
        if (isSyncChangeMethod(aceConfig, method)) {
            boolean async = method.isAnnotationPresent(Async.class);
            return Optional.of(new ChangeMethod(statementName, async, methodSignature, aceConfig, resolveChangeFunction(commandType)));
        }
        return Optional.empty();
    }

    private static boolean isSyncChangeMethod(AceConfig aceConfig, Method method) {
        if (method.isAnnotationPresent(NonBatchMethod.class)) {
            return false;
        }
        List<Class<?>> parameterTypes = Arrays.asList(method.getParameterTypes());
        if (parameterTypes.size() == 1) {
            return Collection.class.isAssignableFrom(parameterTypes.get(0));
        } else {
            return Stream.of(method.getParameterAnnotations())
                    .flatMap(Stream::of)
                    .filter(a -> a.annotationType().equals(Param.class))
                    .map(a -> ((Param) a).value())
                    .anyMatch(v -> v.equals(aceConfig.getListName()));
        }
    }
}
