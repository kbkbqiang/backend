package com.backend.dao.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.backend.dao.aop.BackendDataSourceTransactionManager;
import com.backend.dao.page.interceptor.PageInterceptor;
import com.backend.dao.separate.DynamicDataSource;

/**
 * @ClassName: MyBatisConfig
 * @Description TODO MyBatis基础配置 
 *  springboot集成mybatis的基本入口 
 *  1）创建数据源(如果采用的是默认的tomcat-jdbc数据源，则不需要)
 *  2）创建SqlSessionFactory 3）配置事务管理器，除非需要使用事务，否则不用配置
 * @author zhaoqiang
 * @date: 2016年9月1日 上午9:08:11
 */
@Configuration
@EnableTransactionManagement
//@MapperScan(basePackages = "com.backend.dao.mapper")
public class MyBatisConfig extends DataSourceTransactionManagerAutoConfiguration{

	/**
	 * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
	 * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
	 */
	@Bean
	public DynamicDataSource dataSource(@Qualifier("read") DataSource readDbDataSource, @Qualifier("write") DataSource writeDbDataSource) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put("read", readDbDataSource);
		targetDataSources.put("write", writeDbDataSource);

		DynamicDataSource dataSource = new DynamicDataSource();
		dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
		dataSource.setDefaultTargetDataSource(writeDbDataSource);// 默认的datasource设置为readDbDataSource

		return dataSource;
	}

	/**
	 * 根据数据源创建SqlSessionFactory
	 * @param sqlSessionFactory
	 * @return
	 */
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean(DynamicDataSource ds) {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(ds);
		bean.setTypeAliasesPackage("com.backend.dao.model");

		/*<!-- 4.0.0以后版本可以不设置该参数 -->  
        <property name="dialect" value="oracle"/>  
        <!-- 该参数默认为false -->  
        <!-- 设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用 -->  
        <!-- 和startPage中的pageNum效果一样-->  
        <property name="offsetAsPageNum" value="true"/>  
        <!-- 该参数默认为false -->  
        <!-- 设置为true时，使用RowBounds分页会进行count查询 -->  
        <property name="rowBoundsWithCount" value="true"/>  
        <!-- 设置为true时，如果pageSize=0或者RowBounds.limit = 0就会查询出全部的结果 -->  
        <!-- （相当于没有执行分页查询，但是返回结果仍然是Page类型）-->  
        <property name="pageSizeZero" value="true"/>  
        <!-- 3.3.0版本可用 - 分页参数合理化，默认false禁用 -->  
        <!-- 启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页 -->  
        <!-- 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据 -->  
        <property name="reasonable" value="true"/>  
        <!-- 3.5.0版本可用 - 为了支持startPage(Object params)方法 -->  
        <!-- 增加了一个`params`参数来配置参数映射，用于从Map或ServletRequest中取值 -->  
        <!-- 可以配置pageNum,pageSize,count,pageSizeZero,reasonable,orderBy,不配置映射的用默认值 -->  
        <!-- 不理解该含义的前提下，不要随便复制该配置 -->  
        <property name="params" value="pageNum=start;pageSize=limit;"/>  
        <!-- 支持通过Mapper接口参数来传递分页参数 -->  
        <property name="supportMethodsArguments" value="true"/>  
        <!-- always总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page -->  
        <property name="returnPageInfo" value="check"/>  */
		
		// 分页插件
		/*PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("reasonable", "true");
		properties.setProperty("supportMethodsArguments", "true");
		properties.setProperty("returnPageInfo", "check");
		properties.setProperty("params", "count=countSql");
		pageHelper.setProperties(properties);
		// 添加插件
		bean.setPlugins(new Interceptor[] { pageHelper });*/
		
		// 添加自定义插件
		Properties properties = new Properties();
		properties.setProperty("databaseType", "mysql");
		PageInterceptor pageInterceptor = new PageInterceptor();
		pageInterceptor.setProperties(properties);
		bean.setPlugins(new Interceptor[] {pageInterceptor});

		// 添加XML目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			// bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
			bean.setMapperLocations(resolver.getResources("classpath:com/backend/dao/mapping/*.xml"));
			return bean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	/**
     * 配置事务管理器 自定义事务
     * MyBatis自动参与到spring事务管理中，无需额外配置，只要org.mybatis.spring.SqlSessionFactoryBean引用的数据源与DataSourceTransactionManager引用的数据源一致即可，否则事务管理会不起作用。
     * @return
     */
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManagers(DynamicDataSource dataSource) {
        return new BackendDataSourceTransactionManager(dataSource);
        //return new DataSourceTransactionManager(dataSource);
    }
	
}
