package com.zq.server.config.etc;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 
 * @ClassName: AccountServerProperties 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:39:31 
 */
@Component
@Data
@ConfigurationProperties(ignoreUnknownFields = false,prefix = "account.service")
public class AccountServerProperties {

	private String domain;
    private String app;
    private String certificate;
	
}
