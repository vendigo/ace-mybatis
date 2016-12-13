package com.github.vendigo.acemybatis.config;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

public class AceClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

    public AceClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void registerFilters() {
        addIncludeFilter(new AnnotationTypeFilter(AceMapper.class));
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
        GenericBeanDefinition def;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            def = (GenericBeanDefinition) holder.getBeanDefinition();
            String mapperClassName = def.getBeanClassName();
            def.getConstructorArgumentValues().addGenericArgumentValue(mapperClassName);
            def.getConstructorArgumentValues()
                    .addGenericArgumentValue(new RuntimeBeanReference(resolveSqlSessionFactoryBeanName(mapperClassName)));
            def.setBeanClass(AceMapperFactoryBean.class);
        }
    }

    private String resolveSqlSessionFactoryBeanName(String mapperClassName) {
        Class<?> mapperInterface = null;
        try {
            mapperInterface = Class.forName(mapperClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        AceMapper annotation = mapperInterface.getAnnotation(AceMapper.class);
        return annotation.sqlSessionFactoryBeanName();
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        return super.checkCandidate(beanName, beanDefinition);
    }
}
