package com.github.vendigo.acemybatis.parser;

import com.github.vendigo.acemybatis.config.AceConfig;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class DeclarationParser {
    private static final Logger log = LoggerFactory.getLogger(DeclarationParser.class);

    public static AceMethod parseMethodDeclaration(AceConfig aceConfig, Class<?> mapperInterface,
                                                   SqlSessionFactory sqlSessionFactory, Method method) {
        Configuration config = sqlSessionFactory.getConfiguration();
        MapperMethod mapperMethod = new MapperMethod(mapperInterface, method, config);
        MapperMethod.SqlCommand command = new MapperMethod.SqlCommand(config, mapperInterface, method);
        MapperMethod.MethodSignature methodSignature = new MapperMethod.MethodSignature(config, mapperInterface, method);
        Optional<AceMethod> parsedMethod = Optional.empty();

        switch (command.getType()) {
            case SELECT:
                parsedMethod = parseSelect(methodSignature, config, method, aceConfig);
                break;
            case INSERT:
                parsedMethod = parseChange(methodSignature, config, method, aceConfig, SyncInsert::new, AsyncInsert::new);
                break;
            case UPDATE:
                parsedMethod = parseChange(methodSignature, config, method, aceConfig, SyncUpdate::new, AsyncUpdate::new);
                break;
            case DELETE:
                parsedMethod = parseChange(methodSignature, config, method, aceConfig, SyncDelete::new, AsyncDelete::new);
                break;
        }

        return parsedMethod.orElse(new DelegateMethodImpl(mapperMethod));
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

    private static Optional<AceMethod> parseChange(MapperMethod.MethodSignature methodSignature, Configuration config,
                                                   Method method, AceConfig aceConfig,
                                                   AceMethodSupplier syncMethodSupplier,
                                                   AceMethodSupplier asyncMethodSupplier) {
        if (methodSignature.getReturnType().equals(CompletableFuture.class)) {
            log.info("Using async version for {}", method.getName());
            return Optional.of(asyncMethodSupplier.get(method, methodSignature, aceConfig));
        } else {
            List<Class<?>> parameterTypes = Arrays.asList(method.getParameterTypes());
            if (parameterTypes.size() == 1 && parameterTypes.get(0).equals(List.class)) {
                log.info("Using sync version for {}", method.getName());
                return Optional.of(syncMethodSupplier.get(method, methodSignature, aceConfig));
            }
        }
        return Optional.empty();
    }
}
