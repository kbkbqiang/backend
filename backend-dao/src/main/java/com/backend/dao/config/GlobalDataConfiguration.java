package com.backend.dao.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/** 
 * @ClassName: GlobalDataConfiguration 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 下午4:13:21 
 */
@Configuration  
public class GlobalDataConfiguration {  
//    @Bean(name="primaryDataSource")  
//    @Primary  
//    @ConfigurationProperties(prefix="datasource.primary")  
//    public DataSource primaryDataSource() {  
//        System.out.println("-------------------- primaryDataSource init ---------------------");  
//        return DataSourceBuilder.create().build();  
//    }  
//      
//    @Bean(name="secondaryDataSource")  
//    @ConfigurationProperties(prefix="datasource.secondary")  
//    public DataSource secondaryDataSource() {  
//        System.out.println("-------------------- secondaryDataSource init ---------------------");  
//        return DataSourceBuilder.create().build();  
//    }  
}  