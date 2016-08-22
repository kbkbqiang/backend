package com.zq.server.utils;

import com.zq.server.entity.Session;

/**
 * 
 * @ClassName: TokenHelper
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:30:57
 */
public final class TokenHelper {
	// 也可以用shiro实现本功能
	private static final ThreadLocal<TokenHelper> TokenHolder = new ThreadLocal<TokenHelper>();

	private Session session = null;

	private TokenHelper(Session session) {
		this.session = session;
	}

	public static void init(Session session) {
		TokenHolder.set(new TokenHelper(session));
	}

	public static void destroy() {
		TokenHolder.remove();
	}

	public static Session getToken() {
		return TokenHolder.get().session;
	}
}
