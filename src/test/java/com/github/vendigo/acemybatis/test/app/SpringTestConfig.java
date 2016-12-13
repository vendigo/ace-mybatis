package com.github.vendigo.acemybatis.test.app;

import com.github.vendigo.acemybatis.config.AceMapperScannerConfigurer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.github.vendigo.acemybatis.test.app")
public class SpringTestConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

    @Bean
    public static SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext appContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(appContext.getResources("classpath:*-mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public static AceMapperScannerConfigurer mapperScannerConfigurer() {
        return AceMapperScannerConfigurer.builder()
                .basePackage("com.github.vendigo.acemybatis.test.app")
                .selectChunkSize(2000)
                .updateChunkSize(1000)
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
