package com.backend.dao.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.backend.dao.config.etc.ReadDataSourceProperties;
import com.backend.dao.config.etc.WriteDataSourceProperties;

/** 
 * @ClassName: DataSourceConfig 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 下午4:09:36 
 */
@Configuration
public class DataSourceConfig {
	
	@Autowired
	private ReadDataSourceProperties readDataSourceProperties;
	
	@Autowired
	private WriteDataSourceProperties writeDataSourceProperties;
	
	@Bean(name = "read")
    //@ConfigurationProperties(prefix="spring.datasource.read")
    public DataSource primaryDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(readDataSourceProperties.getDriverClassName());
        dataSource.setUrl(readDataSourceProperties.getUrl());
        dataSource.setUsername(readDataSourceProperties.getUsername());
        dataSource.setPassword(readDataSourceProperties.getPassword());
        return dataSource;
    }

    @Bean(name = "write")
    @Primary
    //@ConfigurationProperties(prefix="spring.datasource.write")
    public DataSource secondaryDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(writeDataSourceProperties.getDriverClassName());
        dataSource.setUrl(writeDataSourceProperties.getUrl());
        dataSource.setUsername(writeDataSourceProperties.getUsername());
        dataSource.setPassword(writeDataSourceProperties.getPassword());
        return dataSource;
        //return DataSourceBuilder.create().build();
    }

}
