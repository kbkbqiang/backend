package com.zq.server.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;

/**
 * 有效IP过虑
 * 
 * @ClassName: IpFilter
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午10:51:37
 */
@Component
public class IpFilter implements Filter {
	HashSet<String> ips = new HashSet<String>();

	public void init(final FilterConfig filterConfig) throws ServletException {
		String ipstr = filterConfig.getInitParameter("allowIps");
		ips.addAll(Arrays.asList(ipstr.split(";")));
	}

	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {
		if (ips.contains(request.getRemoteAddr()))
			filterChain.doFilter(request, response);
	}

	public void destroy() {
		// nothing
	}
}
