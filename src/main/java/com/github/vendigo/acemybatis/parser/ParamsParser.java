package com.github.vendigo.acemybatis.parser;

import com.github.vendigo.acemybatis.config.AceConfig;
import org.apache.ibatis.binding.MapperMethod;

import java.util.*;

public class ParamsParser {
    @SuppressWarnings("unchecked")
    public static ParamsHolder parseParams(AceConfig aceConfig, MapperMethod.MethodSignature methodSignature, Object[] args) {
        Object parsedParams = methodSignature.convertArgsToSqlCommandParam(args);
        Collection<Object> entities;
        Map<String, Object> otherParams = new HashMap<>();
        if (parsedParams instanceof Collection) {
            entities = (Collection<Object>) parsedParams;
        } else if (parsedParams instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>) parsedParams;
            entities = (Collection<Object>) paramMap.remove(aceConfig.getListName());
            otherParams = paramMap;
        } else {
            throw new IllegalArgumentException("Failed to parse parameters");
        }

        List<Object> entitiesAsList = convertToList(entities);
        return new ParamsHolder(entitiesAsList, otherParams);
    }

    private static List<Object> convertToList(Collection<Object> entities) {
        if (entities instanceof List) {
            return (List<Object>)entities;
        }
        List<Object> newList = new ArrayList<>();
        newList.addAll(entities);
        return newList;
    }
}
