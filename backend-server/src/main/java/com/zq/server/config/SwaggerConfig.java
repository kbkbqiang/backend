package com.zq.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * 
 * @ClassName: SwaggerConfig 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月23日 上午11:12:52
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("com.zq.server.controller"))
				.paths(PathSelectors.any()).build().apiInfo(initApiInfo());
	}

	private ApiInfo initApiInfo() {
		ApiInfo apiInfo = new ApiInfo("接口",// 大标题
				"接口", // 简单描述
				"0.1", // 版本
				"服务条款", 
				"zq", // 作者
				"链接文字", // 链接文字
				"http://www.baidu.com" // 网站链接
		);
		return apiInfo;
	}
}
