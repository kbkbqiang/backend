package com.zq.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * @author zhaoqiang
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.zq.service.controller",
        "com.zq.service.service",
        "com.zq.service.config.etc"})
public class SpringWebConfig extends WebMvcConfigurerAdapter {
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 加载swagger页面
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        
    }
	
	@Bean(name = "eventListenerBean")
    public ApplicationListenerBean applicationListenerBean() {
        return new ApplicationListenerBean();
    }

}
