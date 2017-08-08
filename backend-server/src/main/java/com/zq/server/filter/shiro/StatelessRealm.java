package com.zq.server.filter.shiro;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 
 * 
 * @ClassName: StatelessRealm
 * @Description 身份认证realm; (这个需要自己写，账号密码校验；权限等)
 * @author zhaoqiang
 * @date: 2016年8月22日 上午10:50:28
 */
public class StatelessRealm extends AuthorizingRealm {

	// private UserService service = null;

	private Collection<String> permissions = null;

	public StatelessRealm() {
		// service = new UserService();
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		// 仅支持StatelessToken类型的Token
		return token instanceof StatelessToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		if (permissions != null) {
			for (String item : permissions)
				authorizationInfo.addStringPermission(item);
		}

		// Permission permission = new WildcardPermission("account:user:*");
		// authorizationInfo.addObjectPermission(permission);
		// authorizationInfo.addStringPermission("account");
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		StatelessToken stoken = null;
		stoken = (StatelessToken) token;
		String username = stoken.getUsername();

		try {
			// permissions = service.permissionByUser(username);
		} catch (Exception e) {
		}

		return new SimpleAuthenticationInfo(username, stoken.getCredentials(), getName());
	}

	public Collection<String> getPermissions() {
		return permissions;
	}
}
