package com.zq.server.filter.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * 
 * @ClassName: StatelessAuthcFilter 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午10:50:15
 */
public class StatelessAuthcFilter extends AccessControlFilter {
//	@Autowired
	// private UserService userService;
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (httpRequest == null || httpResponse == null) {
			// TokenHelper.destroy();
			return false;
		}
		// 过虑器验证TOKEN是否有效
//		String token = httpRequest.getHeader(ConstString.TokenHeaderName);
//		if (token != null && !token.trim().isEmpty()) {
//			AccountPostResult<AccountTokenResult> result = AccountHttpSender
//					.Instance()
//					.ObjectPost(
//							"/account/check_token",
//							ToJsonUtil.rawJson().put("token", token).toString(),
//							AccountTokenResult.class);
//			if (!result.getResult() || result.getBody() == null) {
//				this.onLoginFail(httpResponse);
//			}
//
//			try {
//				User user = userService.searchUserByEmail(result.getBody()
//						.getUser().getEmail());
//				if (user == null)
//					throw new Exception("");
//
//				Session session = new Session();
//				session.setAccountUser(result.getBody().getUser());
//				session.setUser(user);
//				session.setToken(token);
//
//				TokenHelper.init(session);
//
//				StatelessToken shiro_token = new StatelessToken(user.getId(),
//						user.getId());
//				getSubject(request, response).login(shiro_token);
//			} catch (Exception e) {
//				TokenHelper.destroy();
//				this.onLoginFail(httpResponse);
//				return false;
//			}
//			return true;
//		} else {
//			// 没有token就直接跳到业务代码 和 shiro权限标签处理
//			TokenHelper.destroy();
//			return true;
//		}
		return true;
	}

	// 登录失败时默认返回401状态码
	private void onLoginFail(HttpServletResponse httpResponse){
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		httpResponse.getWriter().write(ToJsonUtil.exceptionToResult("Unauthorized"));
	}
}
