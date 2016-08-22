package com.zq.backend.common.utils;

/**
 * 
 * @ClassName: ValueHolder
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:52:27
 */
public class ValueHolder<T> {
	public ValueHolder(T value) {
		this.value = value;
	}

	private T value;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
