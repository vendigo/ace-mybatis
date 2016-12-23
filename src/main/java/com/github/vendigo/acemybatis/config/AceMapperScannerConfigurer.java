package com.github.vendigo.acemybatis.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static com.github.vendigo.acemybatis.utils.Validator.notNull;

/**
 * Used for automatic discovering/generating ace mappers. Use inner builder for convenient configuring.
 */
public class AceMapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware,
        InitializingBean {
    private final String basePackage;
    private final AceConfig aceConfig;
    private ApplicationContext applicationContext;

    public AceMapperScannerConfigurer(String basePackage, AceConfig aceConfig) {
        this.basePackage = notNull(basePackage);
        this.aceConfig = notNull(aceConfig);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        AceClassPathMapperScanner scanner = new AceClassPathMapperScanner(registry, aceConfig);
        scanner.setResourceLoader(applicationContext);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(basePackage,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // left intentionally blank
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.basePackage, "Property 'basePackage' is required");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String basePackage;
        private AceConfig config;
        private int selectChunkSize;
        private int updateChunkSize;
        private int threadCount;

        public Builder basePackage(String basePackage) {
            this.basePackage = basePackage;
            return this;
        }

        public Builder config(AceConfig config) {
            this.config = config;
            return this;
        }

        public Builder selectChunkSize(int selectChunkSize) {
            this.selectChunkSize = selectChunkSize;
            return this;
        }

        public Builder updateChunkSize(int updateChunkSize) {
            this.updateChunkSize = updateChunkSize;
            return this;
        }

        public Builder threadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }

        public AceMapperScannerConfigurer build() {
            return new AceMapperScannerConfigurer(basePackage, AceConfigResolver.resolveConfig(config,
                    selectChunkSize, updateChunkSize, threadCount));
        }
    }
}
