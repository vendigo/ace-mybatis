package com.github.vendigo.acemybatis.util;

import com.github.vendigo.acemybatis.config.AceMapperScannerConfigurer;
import com.github.vendigo.acemybatis.proxy.AceProxyFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.github.vendigo.acemybatis")
public class SpringTestConfig {
    @Autowired
    ApplicationContext appContext;

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(appContext.getResources("classpath:*-mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public UserMapper userMapper(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new AceProxyFactory<>(UserMapper.class, sqlSessionFactory).newInstance();
    }

    //@Bean
    public AceMapperScannerConfigurer mapperScannerConfigurer(SqlSessionFactory sqlSessionFactory) {
        return new AceMapperScannerConfigurer(sqlSessionFactory, "com.github.vendigo.acemybatis.util");
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
