/**
 * 
 */
package com.zq.server.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @ClassName: Application 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 下午3:57:59
 */
//@EnableTransactionManagement
//@EnableAutoConfiguration
@EnableConfigurationProperties
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
//@EnableAutoConfiguration(exclude={  
//	    DataSourceAutoConfiguration.class,  
//	        HibernateJpaAutoConfiguration.class, //（如果使用Hibernate时，需要加）  
//	        DataSourceTransactionManagerAutoConfiguration.class,  
//	        }) 
public class Application extends SpringBootServletInitializer{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
	
	/**
	 * 暂无test
	 * @return
	 */
	@RequestMapping("/")
    String home() {
        return "redirect:test";
    }

}
