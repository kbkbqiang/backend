package com.zq.server.config.etc;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 
 * @ClassName: IpFilter 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午10:58:04 
 */
@Component
@ConfigurationProperties(ignoreUnknownFields = false,prefix = "corsFilter",locations="classpath:filter.properties")
public class CorsFilterProperties {
	private String allowOrigin;
	private String allowMethods;
	private String allowCredentials;
	private String allowHeaders;
	public String getAllowOrigin() {
		return allowOrigin;
	}
	public void setAllowOrigin(String allowOrigin) {
		this.allowOrigin = allowOrigin;
	}
	public String getAllowMethods() {
		return allowMethods;
	}
	public void setAllowMethods(String allowMethods) {
		this.allowMethods = allowMethods;
	}
	public String getAllowCredentials() {
		return allowCredentials;
	}
	public void setAllowCredentials(String allowCredentials) {
		this.allowCredentials = allowCredentials;
	}
	public String getAllowHeaders() {
		return allowHeaders;
	}
	public void setAllowHeaders(String allowHeaders) {
		this.allowHeaders = allowHeaders;
	}
}
