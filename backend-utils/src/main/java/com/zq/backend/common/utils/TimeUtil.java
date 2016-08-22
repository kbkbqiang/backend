package com.zq.backend.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * @ClassName: TimeUtil
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:51:55
 */
public class TimeUtil {
	private static final String DateFmt = "yyyy-MM-dd HH:mm:ss";
	private static final String YearFmt = "yyyy";
	private static final String MonthFmt = "MM";
	private static final String DayFmt = "dd";

	// utc timestramp
	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public static String getCurrentYear() {
		SimpleDateFormat sdf = new SimpleDateFormat(YearFmt);
		Date resultdate = new Date(getCurrentTime());
		return sdf.format(resultdate);
	}

	public static String getCurrentMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat(MonthFmt);
		Date resultdate = new Date(getCurrentTime());
		return sdf.format(resultdate);
	}

	public static String getCurrentDay() {
		SimpleDateFormat sdf = new SimpleDateFormat(DayFmt);
		Date resultdate = new Date(getCurrentTime());
		return sdf.format(resultdate);
	}

	public static String formatToLocalTime(long ts) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateFmt);
		Date resultdate = new Date(ts);
		return sdf.format(resultdate);
	}

	public static String formatToUtcTime(long ts) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateFmt);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date resultdate = new Date(ts);
		return sdf.format(resultdate);
	}

	public static Long Parse(String str) {
		DateFormat df = new SimpleDateFormat(DateFmt);
		try {
			Date result = df.parse(str);
			return result.getTime();
		} catch (ParseException e) {
			return null;
		}
	}

	public static long offsetSecond(long time) {
		return (getCurrentTime() - time) / 1000;
	}

	public static long offsetSecond(long newtime, long oldtime) {
		return (newtime - oldtime) / 1000;
	}

	/**
	 * 取得系统当前时间
	 * 
	 * @return String yyyyMMddHhmmss
	 */
	public static String getCurrentDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		java.util.Date date = new java.util.Date();
		String time = simpleDateFormat.format(date);
		return time;
	}
}
