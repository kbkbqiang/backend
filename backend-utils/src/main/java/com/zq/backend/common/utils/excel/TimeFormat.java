package com.zq.backend.common.utils.excel;

import java.util.Date;

import org.springframework.util.StringUtils;

import com.zq.backend.common.utils.DateUtils;


/**
 * 日期格式化，按传入的格式化字符串formatStr进行格式化
 * 
 * @author jiangping
 * 
 */
@SuppressWarnings("rawtypes")
public class TimeFormat implements DataFormat {

	@Override
	public String format(Object value, Object t, Object formatStr) {
		if (null == value) {
			return "";
		}
		if (value instanceof Date) {
			if (StringUtils.isEmpty(formatStr)) {
				formatStr = DateUtils.SHOW_DATE_FORMAT;
			}
			return DateUtils.format((Date) value, (String) formatStr);
		}
		return value.toString();
	}

}
