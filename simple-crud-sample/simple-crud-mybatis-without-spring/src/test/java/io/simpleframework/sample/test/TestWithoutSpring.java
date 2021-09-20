package io.simpleframework.sample.test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.simpleframework.crud.DatasourceProvider;
import io.simpleframework.crud.Models;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Test;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class TestWithoutSpring {
    private final static SqlSessionFactory sqlSessionFactory;
    private final static DatasourceProvider provider;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setUsername("work");
        hikariConfig.setPassword("123456");
        hikariConfig.setJdbcUrl("jdbc:mysql://192.168.55.111:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=PRC");
        Environment environment = new Environment("demo",
                new JdbcTransactionFactory(),
                new HikariDataSource(hikariConfig));

        Configuration config = new Configuration(environment);
        config.setLogImpl(StdOutImpl.class);
        sqlSessionFactory = new DefaultSqlSessionFactory(config);

        provider = new DatasourceProvider() {

            @Override
            public SqlSession mybatisSqlSession(String name) {
                return sqlSessionFactory.openSession(true);
            }

            @Override
            public boolean mybatisSqlSessionCloseable(String name) {
                return true;
            }
        };
    }

    @Test
    public void test() {
        Models.datasourceProvider(provider);

        BasicQueryTest.test();

        BasicCommandTest.test();
    }

}
