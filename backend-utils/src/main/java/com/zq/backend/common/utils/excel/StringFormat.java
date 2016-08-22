package com.zq.backend.common.utils.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
/**
 * 字符串格式化
 * @author jiangping
 *
 */
public class StringFormat implements DataFormat {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String format(Object value, Object t, Object object) {
		
		if (null==value||(value instanceof String && StringUtils.isEmpty((String)value))) {
			return "";
		}
		String fstr = (String) object;
		String res = String.valueOf(value);
		try{
		 res =  String.format(fstr,  value);
		}catch(Exception e){
			logger.error("字符串格式化异常：",e);
		}
		return res;
	}

}
