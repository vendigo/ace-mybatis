package com.github.vendigo.acemybatis.parser;

import org.apache.ibatis.binding.MapperMethod;

import java.util.Collection;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;

public class ParameterParser {

    public static final String ENTITIES_KEY = "entities";

    @SuppressWarnings("unchecked")
    public static ParamsHolder parseParams(MapperMethod.MethodSignature methodSignature, Object[] args) {
        Object parsedParams = methodSignature.convertArgsToSqlCommandParam(args);
        Collection<Object> entities;
        Map<String, Object> otherParams = new HashMap<>();
        if (parsedParams instanceof Collection) {
            entities = (Collection<Object>)parsedParams;
        } else if (parsedParams instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>)parsedParams;
            entities = (Collection<Object>)paramMap.remove(ENTITIES_KEY);
        } else {
            throw new IllegalArgumentException("Failed to parse parameters");
        }

        return new ParamsHolder(entities, otherParams);
    }
}
