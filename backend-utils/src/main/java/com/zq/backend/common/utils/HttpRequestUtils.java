package com.zq.backend.common.utils;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName: HttpRequestUtils 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:46:06
 */
public class HttpRequestUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class); // 日志记录

	/**
	 * httpPost
	 * 
	 * @param url
	 *            路径
	 * @param jsonParam
	 *            参数
	 * @return
	 */
	public static String httpPost(String url, String jsonParam) {
		return httpPost(url, jsonParam, false);
	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @param noNeedResponse
	 *            不需要返回结果
	 * @return
	 */
	public static String httpPost(String url, String jsonParam, boolean noNeedResponse) {
		// post请求返回结果
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String jsonResult = null;
		HttpPost method = new HttpPost(url);
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			CloseableHttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200) {
				try {
					/** 读取服务器返回过来的json字符串数据 **/
					jsonResult = EntityUtils.toString(result.getEntity(), HTTP.UTF_8);
					if (noNeedResponse) {
						return null;
					}
				} catch (Exception e) {
					logger.error("post请求提交失败:" + url, e);
					return ToJsonUtil.exceptionToResult(e);
				}
			}
		} catch (IOException e) {
			logger.error("post请求提交失败:" + url, e);
			return ToJsonUtil.exceptionToResult(e);
		}
		return jsonResult;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 *            路径
	 * @return
	 */
	public static String httpGet(String url) {
		// get请求返回结果
		String jsonResult = null;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			// 发送get请求
			HttpGet request = new HttpGet(url);
			CloseableHttpResponse response = client.execute(request);

			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				/** 读取服务器返回过来的json字符串数据 **/
				jsonResult = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
				url = URLDecoder.decode(url, "UTF-8");
			} else {
				logger.error("get请求提交失败:" + url);
			}
		} catch (IOException e) {
			logger.error("get请求提交失败:" + url, e);
			return ToJsonUtil.exceptionToResult(e);
		}
		return jsonResult;
	}

	/**
	 * put请求
	 * 
	 * @param url
	 *            地址
	 * @param jsonParam
	 *            put参数
	 * @return
	 */
	public static String httpPut(String url, String jsonParam) {
		return httpPut(url, jsonParam, false);
	}

	/**
	 * put请求
	 * 
	 * @param url
	 *            地址
	 * @param jsonParam
	 *            put参数
	 * @param noNeedResponse
	 *            是否返回结果
	 * @return
	 */
	public static String httpPut(String url, String jsonParam, boolean noNeedResponse) {
		// put请求返回结果
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String jsonResult = null;
		HttpPut method = new HttpPut(url);
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			CloseableHttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200) {
				try {
					/** 读取服务器返回过来的json字符串数据 **/
					jsonResult = EntityUtils.toString(result.getEntity(), HTTP.UTF_8);
					if (noNeedResponse) {
						return null;
					}
				} catch (Exception e) {
					logger.error("put请求提交失败:" + url, e);
					return ToJsonUtil.exceptionToResult(e);
				}
			}
		} catch (IOException e) {
			logger.error("put请求提交失败:" + url, e);
			return ToJsonUtil.exceptionToResult(e);
		}
		return jsonResult;
	}

	/**
	 * delete请求
	 * 
	 * @param url
	 *            地址
	 * @param jsonParam
	 *            delete参数
	 * @return
	 */
	public static String httpDelete(String url, String jsonParam) {
		return httpDelete(url, jsonParam, false);
	}

	/**
	 * delete请求
	 * 
	 * @param url
	 *            地址
	 * @param jsonParam
	 *            delete参数
	 * @param noNeedResponse
	 *            是否返回结果
	 * @return
	 */
	public static String httpDelete(String url, String jsonParam, boolean noNeedResponse) {
		// delete请求返回结果
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String jsonResult = null;
		HttpDeleteWithBody method = new HttpDeleteWithBody(url);
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			CloseableHttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200) {
				try {
					/** 读取服务器返回过来的json字符串数据 **/
					jsonResult = EntityUtils.toString(result.getEntity(), HTTP.UTF_8);
					if (noNeedResponse) {
						return null;
					}
				} catch (Exception e) {
					logger.error("delete请求提交失败:" + url, e);
					return ToJsonUtil.exceptionToResult(e);
				}
			}
		} catch (IOException e) {
			logger.error("delete请求提交失败:" + url, e);
			return ToJsonUtil.exceptionToResult(e);
		}
		return jsonResult;
	}
}
