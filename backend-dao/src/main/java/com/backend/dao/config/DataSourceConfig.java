package com.backend.dao.config;

import javax.inject.Qualifier;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/** 
 * @ClassName: DataSourceConfig 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 下午4:09:36 
 */
@Configuration
public class DataSourceConfig {
	
//	@Bean(name = "primaryDataSource")
//    @Qualifier("primaryDataSource")
//    @ConfigurationProperties(prefix="spring.datasource.primary")
//    public DataSource primaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "secondaryDataSource")
//    @Qualifier("secondaryDataSource")
//    @Primary
//    @ConfigurationProperties(prefix="spring.datasource.secondary")
//    public DataSource secondaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }

}
