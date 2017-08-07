package com.zq.server.config.etc;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 
 * @ClassName: IpFilterProperties 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:07:04 
 */
@Component
@ConfigurationProperties(ignoreUnknownFields = false,prefix = "ipFilter",locations="classpath:filter.properties")
public class IpFilterProperties {

	private String allowIps;

	public String getAllowIps() {
		return allowIps;
	}

	public void setAllowIps(String allowIps) {
		this.allowIps = allowIps;
	}
	
	
}
