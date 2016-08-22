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
@Data
@ConfigurationProperties(ignoreUnknownFields = false,prefix = "corsFilter",locations="classpath:filter.properties")
public class CorsFilterProperties {
	private String allowOrigin;
	private String allowMethods;
	private String allowCredentials;
	private String allowHeaders;
}
