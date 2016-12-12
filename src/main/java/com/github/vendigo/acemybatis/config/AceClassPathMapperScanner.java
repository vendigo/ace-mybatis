package com.github.vendigo.acemybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

public class AceClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
    private final Class<? extends Annotation> annotationClass;
    private final SqlSessionFactory sqlSessionFactory;

    public AceClassPathMapperScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annotationClass,
                                     SqlSessionFactory sqlSessionFactory) {
        super(registry, false);
        this.annotationClass = annotationClass;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void registerFilters() {
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        } else {
            addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        }

        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (!beanDefinitions.isEmpty()) {
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.getConstructorArgumentValues().addGenericArgumentValue(sqlSessionFactory);
            definition.setBeanClass(AceMapperFactoryBean.class);
        }
    }
}
