package com.cloudSeckill.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.cloudSeckill","com.proxy"}, sqlSessionFactoryRef = "paySqlSessionFactory")
public class DataSourcesConfig {
    @Value("${master.pay.datasource.url}")
    private String dbUrl;
    @Value("${master.pay.datasource.username}")
    private String dbUser;
    @Value("${master.pay.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.max-idle:80}")
    private Integer dataSourceMaxIdle;

    @Value("${spring.datasource.min-idle:5}")
    private Integer dataSourceMinIdle;

    @Value("${spring.datasource.max-wait:1000}")
    private Long dataSourceMaxWait;

    @Value("${spring.datasource.initial-size:10}")
    private Integer initialSize;

    @Bean(name = "payDataSource")
    public DataSource payDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        dataSource.setMinIdle(dataSourceMinIdle);
        dataSource.setMaxWait(dataSourceMaxWait);
//        dataSource.setMaxIdle(dataSourceMaxIdle);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(dataSourceMaxIdle);
        return dataSource;
    }

    @Bean(name = "payTransactionManager")
    public DataSourceTransactionManager payTransactionManager() {
        return new DataSourceTransactionManager(payDataSource());
    }

    @Bean(name = "paySqlSessionFactory")
    public SqlSessionFactory paySqlSessionFactory(@Qualifier("payDataSource") DataSource payDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(payDataSource);
        return sessionFactory.getObject();
    }
}
