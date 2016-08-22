package com.zq.backend.common.utils.excel;

/**
 * 数据格式化接口，数据格式化后通过format方法返回格式化后的值
 * 
 * @author jiangping
 * 
 */
public interface DataFormat<T> {

	/**
	 *  格式化方法
	 * @param value	单元格值
	 * @param t		本行对象
	 * @param object	格式化参数
	 * @return
	 */
	public String format(Object value, T t, Object object);
}
