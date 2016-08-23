package com.backend.dao.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
	
	@Bean(name = "read")
    @Qualifier("read")
	@Primary
    @ConfigurationProperties(prefix="spring.datasource.read")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "write")
    @Qualifier("write")
    @ConfigurationProperties(prefix="spring.datasource.write")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

}
