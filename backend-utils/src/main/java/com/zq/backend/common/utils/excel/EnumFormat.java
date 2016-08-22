package com.zq.backend.common.utils.excel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 要求Object formatParam参数符合要求，要求如下：
 * 1.第一个值为枚举的class,第二个值为要获取的域名（例如为name），第三个值为value在枚举中的域名(例如为code)
 * 2.枚举中必须要有指定域名的get方法（例如:getName和getCode方法）
 * 
 * @author jiangping
 * 
 */
public class EnumFormat implements DataFormat {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 遍历枚举类型获取对应的值 formatParam
	 * 该参数为一个数组，第一个值为枚举的class,第二个值为要获取的域名，第二个值为value在枚举中的域名
	 */
	@Override
	public String format(Object value, Object t, Object formatParam) {
		// 若参数是数组，处理枚举情况
		if (null != formatParam && formatParam instanceof Object[]) {
			// 检验参数个数，若小于三个则不处理
			Object[] args = (Object[]) formatParam;
			if (null == args || args.length < 3) {
				return null != value ? value.toString() : "";
			}
			try {
				// 若第一个类型不是枚举类型则直接返回
				if (!(args[0] instanceof Class)) {
					return null != value ? value.toString() : "";
				}
				Class clazz = (Class) args[0];
				String toMeth = (String) args[1];
				String fromMeth = (String) args[2];
				// 获取来源域名和转换域名对应的get方法
				toMeth = null != toMeth && toMeth.length() > 0 ? toMeth
						.substring(0, 1).toUpperCase() + toMeth.substring(1)
						: toMeth;
				fromMeth = null != fromMeth && fromMeth.length() > 0 ? fromMeth
						.substring(0, 1).toUpperCase() + fromMeth.substring(1)
						: toMeth;
				Method methodTo = clazz.getMethod("get" + toMeth);
				Method methodFrom = clazz.getMethod("get" + fromMeth);
				// 获取枚举的所有实例，遍历
				Object[] objs = clazz.getEnumConstants();
				if (null == objs || objs.length < 1) {
					return "";
				}
				for (Object obj : objs) {
					Object v = methodFrom.invoke(obj);
					if ((v instanceof Integer || value instanceof Integer)
							&& (v instanceof Byte || value instanceof Byte)) {
						int v2 = v instanceof Byte ? (byte) v : (int) v;
						int v3 = value instanceof Byte ? (byte) value
								: (int) value;
						if (v2 == v3) {
							return (String) methodTo.invoke(obj);
						} else {
							continue;
						}
					}
					if (v.equals(value)) {
						return (String) methodTo.invoke(obj);
					}

				}
			} catch (NoSuchMethodException e) {
				logger.error("导出Excel时，使用EnumFormat格式化时异常：",e);
			} catch (SecurityException e) {
				logger.error("导出Excel时，使用EnumFormat格式化时异常：",e);
			} catch (IllegalAccessException e) {
				logger.error("导出Excel时，使用EnumFormat格式化时异常：",e);
			} catch (IllegalArgumentException e) {
				logger.error("导出Excel时，使用EnumFormat格式化时异常：",e);
			} catch (InvocationTargetException e) {
				logger.error("导出Excel时，使用EnumFormat格式化时异常：",e);
			}

		}
		return null != value ? value.toString() : "";
	}

}