package com.zq.backend.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * 
 * @ClassName: PropsUtil
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:47:14
 */
public final class PropsUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

	/**
	 * 加载属性文件
	 */
	public static Properties loadProps(String fileName) {
		Properties props = null;
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (is == null) {
				throw new FileNotFoundException(fileName + " file is not found");
			}
			props = new Properties();
			props.load(new InputStreamReader(is, "UTF-8"));

			// 转码处理
			Set<Object> keyset = props.keySet();
			Iterator<Object> iter = keyset.iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				String newValue = null;
				newValue = new String(props.getProperty(key));
				/*
				 * try { //属性配置文件自身的编码 String propertiesFileEncode = "utf-8";
				 * newValue = new
				 * String(props.getProperty(key).getBytes("ISO8859-1"
				 * ),propertiesFileEncode); } catch
				 * (UnsupportedEncodingException e) { newValue =
				 * props.getProperty(key); }
				 */
				props.setProperty(key, newValue);
			}

		} catch (IOException e) {
			LOGGER.error("load properties file failure", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error("close input stream failure", e);
				}
			}
		}
		return props;
	}

	/**
	 * 获取字符型属性（默认值为空字符串）
	 */
	public static String getString(Properties props, String key) {
		return getString(props, key, "");
	}

	/**
	 * 获取字符型属性（可指定默认值）
	 */
	public static String getString(Properties props, String key, String defaultValue) {
		String value = defaultValue;
		if (props.containsKey(key)) {
			value = props.getProperty(key);
		}
		return value;
	}

	/**
	 * 获取数值型属性（默认值为 0）
	 */
	public static int getInt(Properties props, String key) {
		return getInt(props, key, 0);
	}

	// 获取数值型属性（可指定默认值）
	public static int getInt(Properties props, String key, int defaultValue) {
		int value = defaultValue;
		if (props.containsKey(key)) {
			value = CastUtil.castInt(props.getProperty(key));
		}
		return value;
	}

	/**
	 * 获取布尔型属性（默认值为 false）
	 */
	public static boolean getBoolean(Properties props, String key) {
		return getBoolean(props, key, false);
	}

	/**
	 * 获取布尔型属性（可指定默认值）
	 */
	public static boolean getBoolean(Properties props, String key, boolean defaultValue) {
		boolean value = defaultValue;
		if (props.containsKey(key)) {
			value = CastUtil.castBoolean(props.getProperty(key));
		}
		return value;
	}
}
