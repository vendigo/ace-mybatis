package com.github.vendigo.acemybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

public class AceMapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor {
    private final Class<? extends Annotation> annotationClass;
    private final SqlSessionFactory sqlSessionFactory;
    private final String basePackage;

    public AceMapperScannerConfigurer(Class<? extends Annotation> annotationClass, SqlSessionFactory sqlSessionFactory,
                                      String basePackage) {
        this.annotationClass = annotationClass;
        this.sqlSessionFactory = sqlSessionFactory;
        this.basePackage = basePackage;
    }

    public AceMapperScannerConfigurer(SqlSessionFactory sqlSessionFactory,
                                      String basePackage) {
        this.annotationClass = null;
        this.sqlSessionFactory = sqlSessionFactory;
        this.basePackage = basePackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        AceClassPathMapperScanner scanner = new AceClassPathMapperScanner(registry, annotationClass, sqlSessionFactory);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(basePackage,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // left intentionally blank
    }
}
