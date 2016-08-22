package com.zq.server.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zq.server.config.etc.CorsFilterProperties;
import com.zq.server.config.etc.IpFilterProperties;
import com.zq.server.filter.CharsetFilter;
import com.zq.server.filter.CorsFilter;
import com.zq.server.filter.IpFilter;

/**
 * 
 * 
 * @ClassName: SpringWebConfig 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 下午3:45:50
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.zq.server.controller",
		"com.zq.server.service",
		"com.zq.server.filter",
		"com.zq.server.config.etc" })
public class SpringWebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private CorsFilterProperties corsFilterProperties;
	
	@Autowired
	private IpFilterProperties ipFilterProperties;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 加载swagger页面
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	/**
	 * filter
	 */
	@Bean(name = "ipFilter")
	public Filter ipFilter() {
		return new IpFilter();
	}

	@Bean
	public FilterRegistrationBean ipFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(ipFilter());
		registration.addUrlPatterns("/init/*");
		registration.addInitParameter("allowIps", ipFilterProperties.getAllowIps());
		registration.setName("ipFilter");
		return registration;
	}

	@Bean(name = "charsetFilter")
	public Filter charsetFilter() {
		return new CharsetFilter();
	}

	@Bean
	public FilterRegistrationBean charsetFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(charsetFilter());
		registration.addUrlPatterns("/*");
		registration.setName("charsetFilter");
		return registration;
	}

	@Bean(name = "corsFilter")
	public Filter corsFilter() {
		return new CorsFilter();
	}

	@Bean
	public FilterRegistrationBean corsFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(corsFilter());
		registration.addUrlPatterns("/*");
		registration.setName("corsFilter");
		registration.addInitParameter("allowOrigin", corsFilterProperties.getAllowOrigin());
		registration.addInitParameter("allowMethods", corsFilterProperties.getAllowMethods());
		registration.addInitParameter("allowCredentials", corsFilterProperties.getAllowCredentials());
		registration.addInitParameter("allowHeaders", corsFilterProperties.getAllowHeaders());
		return registration;
	}

	@Bean(name = "eventListenerBean")
	public ApplicationListenerBean applicationListenerBean() {
		return new ApplicationListenerBean();
	}

}
