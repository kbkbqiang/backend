package com.zq.backend.common.utils.json;

/**
 * 
 * @ClassName: JArrayInterface 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:36:07
 */
public interface JArrayInterface {
	public JArrayInterface add(String value);

	public JArrayInterface add(boolean value);

	public JArrayInterface add(int value);

	public JArrayInterface add(long value);

	public JArrayInterface add(float value);

	public JArrayInterface add(double value);

	public JArrayInterface add(JObjectInterface value);

	public JObjectInterface newObject();

	public JArrayInterface newArray();
}
