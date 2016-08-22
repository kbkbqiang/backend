package com.zq.backend.common.utils.excel;

import java.util.Map;

/**
 * 键值对格式话，从map中取得key=value的值返回
 * 
 * @author niiwoo
 * 
 */
@SuppressWarnings("rawtypes")
public class MapFormat implements DataFormat {

	@Override
	public String format(Object value, Object t, Object object) {
		if (object instanceof Map) {
			Map map = (Map) object;
			Object res = map.get(value);
			if (null != res) {
				return String.valueOf(res);
			}
		}
		return "";
	}

}
