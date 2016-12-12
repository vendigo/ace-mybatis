package com.github.vendigo.acemybatis.parser;

import com.github.vendigo.acemybatis.config.AceConfig;
import com.github.vendigo.acemybatis.method.AceMethod;
import org.apache.ibatis.binding.MapperMethod;

import java.lang.reflect.Method;

@FunctionalInterface
public interface AceMethodSupplier {
    AceMethod get(Method method, MapperMethod.MethodSignature methodSignature, AceConfig aceConfig);
}
