package com.github.vendigo.acemybatis.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Together with {@link AceMapperScannerConfigurer} is used for automatic discovering/creating ace mappers.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AceMapper {
    /**
     * Bean name for sqlSessionFactory which will be used for particular mapper.
     * If not defined, it will search for only SqlSessionFactory bean in the context.
     */
    String sqlSessionFactoryBeanName() default "";
}
