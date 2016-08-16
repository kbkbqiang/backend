/**
 * 
 */
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
 * @author zhaoqiang
 *
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("cn.zq"))
				.paths(PathSelectors.any()).build().apiInfo(initApiInfo());
	}

	private ApiInfo initApiInfo() {
		ApiInfo apiInfo = new ApiInfo("后端API 接口",// 大标题
				"API 接口",// 简单的描述
				"0.1",// 版本
				"服务条款", "",// 作者
				"",// 链接显示文字
				""// 网站链接
		);
		return apiInfo;
	}
}
