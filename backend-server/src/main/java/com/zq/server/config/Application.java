/**
 * 
 */
package com.zq.server.config;

import javax.inject.Qualifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * 
 * @ClassName: Application 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 下午3:57:59
 */
@EnableAutoConfiguration
@EnableConfigurationProperties
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

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
}
