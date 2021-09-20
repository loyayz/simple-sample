package io.simpleframework.sample;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@EnableConfigurationProperties({MybatisProperties.class})
@Configuration
@RequiredArgsConstructor
public class MybatisConfig {
    public static final String DS_ONE = "oneSqlSessionTemplate";
    public static final String DS_TWO = "twoSqlSessionTemplate";
    private final MybatisProperties properties;

    @Bean(name = "oneDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.one")
    @Primary
    public DataSource oneDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oneSqlSessionFactory")
    @Primary
    public SqlSessionFactory oneSqlSessionFactory() throws Exception {
        return this.buildSqlSessionFactory(this.oneDataSource());
    }

    @Bean(name = DS_ONE)
    @Primary
    public SqlSessionTemplate oneSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(this.oneSqlSessionFactory());
    }

    @Bean(name = "twoDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.two")
    public DataSource twoDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "twoSqlSessionFactory")
    public SqlSessionFactory twoSqlSessionFactory() throws Exception {
        return this.buildSqlSessionFactory(this.twoDataSource());
    }

    @Bean(name = DS_TWO)
    public SqlSessionTemplate twoSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(this.twoSqlSessionFactory());
    }

    public SqlSessionFactory buildSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);

        org.apache.ibatis.session.Configuration configuration = this.properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
            configuration = new org.apache.ibatis.session.Configuration();
        }
        factory.setConfiguration(configuration);

        if (this.properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(this.properties.getConfigurationProperties());
        }
        return factory.getObject();
    }

}
