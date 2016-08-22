package com.zq.backend.common.utils;

import java.util.Collection;

/**
 * 
 * @ClassName: PermissionUtil
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:47:05
 */
public class PermissionUtil {
	private static String perm_dele = ":";

	public static String getPerm_dele() {
		return perm_dele;
	}

	public static boolean isParent(String parent, String child) {
		assert parent != null && !parent.isEmpty() && child != null && !child.isEmpty();
		if (parent.length() + 1 < child.length()) {
			if (child.startsWith(parent + perm_dele))
				return true;
		}
		return false;
	}

	public static String getParent(String perm) {
		if (perm == null || perm.isEmpty())
			return null;
		int lastpos = perm.lastIndexOf(perm_dele);
		if (lastpos <= 0)
			return null;
		return perm.substring(0, lastpos);
	}

	// 是否具有权限（拥有）
	public static boolean ownPermission(Collection<String> permissions, String permission) {
		assert permissions != null && !permissions.isEmpty() && permission != null && !permission.isEmpty();
		if (permissions.contains(permission))
			return true;
		for (String item : permissions)
			if (isParent(item, permission))
				return true;
		return false;
	}

	// 是否具有该权限，或者任意子权限
	public static boolean anyPermission(Collection<String> permissions, String permission) {
		assert permissions != null && !permissions.isEmpty() && permission != null && !permission.isEmpty();
		if (permissions.contains(permission))
			return true;
		for (String item : permissions) {
			if (isParent(permission, item) || isParent(item, permission)) {
				return true;
			}
		}
		return false;
	}
}
