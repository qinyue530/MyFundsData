package com.example.myfunds.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * MyBatis配置类，显式配置SqlSessionFactory和事务管理器
 */
@Configuration
public class MyBatisConfig {
    
    /**
     * 配置SqlSessionFactory，显式指定mapper.xml文件位置
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        
        // 显式设置mapper.xml文件的位置
        factoryBean.setMapperLocations(
            new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml")
        );
        
        // 设置实体类别名包路径
        factoryBean.setTypeAliasesPackage("com.example.myfunds.entity");
        
        // 获取SqlSessionFactory实例
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        
        // 配置驼峰命名转换
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        
        return sqlSessionFactory;
    }
    
    /**
     * 配置事务管理器
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}