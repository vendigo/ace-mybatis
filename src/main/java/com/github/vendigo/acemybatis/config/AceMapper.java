package com.github.vendigo.acemybatis.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AceMapper {
    String sqlSessionFactoryBeanName() default "sqlSessionFactory";
}
