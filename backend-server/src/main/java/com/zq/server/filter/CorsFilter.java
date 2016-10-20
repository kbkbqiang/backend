package com.zq.server.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 设置跨域
 * 
 * @ClassName: CorsFilter
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午10:51:29
 */
public class CorsFilter implements Filter {

	private String allowOrigin;
	private String allowMethods;
	private String allowCredentials;
	private String allowHeaders;
	private String exposeHeaders;

	public void init(FilterConfig filterConfig) throws ServletException {
		allowOrigin = filterConfig.getInitParameter("allowOrigin");
		allowMethods = filterConfig.getInitParameter("allowMethods");
		allowCredentials = filterConfig.getInitParameter("allowCredentials");
		allowHeaders = filterConfig.getInitParameter("allowHeaders");
		exposeHeaders = filterConfig.getInitParameter("exposeHeaders");
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (StringUtils.isNotEmpty(allowOrigin)) {
			List<String> allowOriginList = Arrays.asList(allowOrigin.split(","));
			if (CollectionUtils.isNotEmpty(allowOriginList)) {
				String currentOrigin = request.getHeader("Origin");
				if (currentOrigin != null) {
					for (String item : allowOriginList) {
						if (currentOrigin.equals(item) || currentOrigin.startsWith(item + ":") || currentOrigin.startsWith(item + "/"))
							response.setHeader("Access-Control-Allow-Origin", currentOrigin);
					}
				}
			}
		}
		if (StringUtils.isNotEmpty(allowMethods)) {
			response.setHeader("Access-Control-Allow-Methods", allowMethods);
		}
		if (StringUtils.isNotEmpty(allowCredentials)) {
			response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
		}
		if (StringUtils.isNotEmpty(allowHeaders)) {
			response.setHeader("Access-Control-Allow-Headers", allowHeaders);
		}
		if (StringUtils.isNotEmpty(exposeHeaders)) {
			response.setHeader("Access-Control-Expose-Headers", exposeHeaders);
		}
		try {
			chain.doFilter(req, res);
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public void destroy() {
	}
}
