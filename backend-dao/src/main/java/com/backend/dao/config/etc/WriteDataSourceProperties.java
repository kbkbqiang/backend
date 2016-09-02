package com.backend.dao.config.etc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 
 * @ClassName: ReadDataSourceProperties 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年9月1日 上午11:44:23 
 */
@Component
@ConfigurationProperties(ignoreUnknownFields = false,prefix = "spring.datasource.write",locations="classpath:jdbc.properties")
public class WriteDataSourceProperties {
	
	private String url;
	private String username;
	private String password;
	private String driverClassName;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

}
