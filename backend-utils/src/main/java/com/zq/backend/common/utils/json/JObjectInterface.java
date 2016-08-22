package com.zq.backend.common.utils.json;

import java.util.Collection;

/**
 * 
 * @ClassName: JObjectInterface
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:35:19
 */
public interface JObjectInterface {
	
	public JObjectInterface put(String key, String value);

	public JObjectInterface put(String key, boolean value);

	public JObjectInterface put(String key, int value);

	public JObjectInterface put(String key, long value);

	public JObjectInterface put(String key, float value);

	public JObjectInterface put(String key, double value);

	public JObjectInterface put(String key, JArrayInterface value);

	public JObjectInterface put(String key, JObjectInterface value);

	public JObjectInterface put(String key, ToJsonInterface obj, String user_arg);

	public JObjectInterface put(ToJsonInterface obj, String user_arg);

	public JObjectInterface put(String key, Collection<? extends ToJsonInterface> objs, String user_arg);

	public JObjectInterface newObject();

	public JArrayInterface newArray();
}
