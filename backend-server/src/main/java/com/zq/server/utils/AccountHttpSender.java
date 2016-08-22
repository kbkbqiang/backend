package com.zq.server.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import com.zq.backend.common.utils.SHA1RSASignature;
import com.zq.backend.common.utils.json.JObjectReader;
import com.zq.server.config.etc.AccountServerProperties;

/**
 * 
 * @ClassName: AccountHttpSender
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午11:30:40
 */
public class AccountHttpSender {
	public static class DummyType {
	}

	public static void setSingleton(AccountServerProperties accountServerPropertiesp) {
		AccountHttpSender.singleton = new AccountHttpSender(accountServerPropertiesp);
	}

	public static AccountHttpSender Instance() {
		return AccountHttpSender.singleton;
	}

	private volatile static AccountHttpSender singleton;

	AccountServerProperties accountServerProperties;

	private AccountHttpSender(AccountServerProperties accountServerPropertiesp) {
		this.accountServerProperties = accountServerPropertiesp;
	}

	public <T> AccountPostResult<T> ObjectPost(String path, String content, Class<T> type) {
		AccountPostResult<T> result = new AccountPostResult<>();
		String resultJson = "";
		try {
			resultJson = this.Post(path, content);
		} catch (Exception e) {
			result.setBody(null);
			result.setMessage(e.getMessage());
			result.setResult(false);
		}

		try {
			JObjectReader jreader = JObjectReader.createJObjectReader(resultJson);

			// result
			String resultStr = jreader.readNoneEmptyString("result");
			result.setResult(resultStr != null && resultStr.equals("succuss"));

			// message
			if (jreader.pathExist("message")) {
				result.setMessage(jreader.readString("message"));
			}
			if (result.getResult() != true) {
				if (StringUtils.isEmpty(result.getMessage()))
					result.setMessage(resultJson);
			}

			// data
			if (type != DummyType.class && jreader.pathExist("body"))
				result.setBody(jreader.readObject("body", type));
		} catch (Exception e) {
			result.setBody(null);
			result.setMessage("invalid json result '" + resultJson + "'");
			result.setResult(false);
		}
		return result;
	}

	public <T> Account2PostResult<T> ArrayPost(String path, String content, String respKey, Class<T> type) {
		Account2PostResult<T> result = new Account2PostResult<>();

		String resultJson = "";
		try {
			resultJson = this.Post(path, content);
		} catch (Exception e) {
			result.setBody(null);
			result.setMessage(e.getMessage());
			result.setResult(false);
		}

		try {
			JObjectReader jreader = JObjectReader.createJObjectReader(resultJson);

			// result
			String resultStr = jreader.readNoneEmptyString("result");
			result.setResult(resultStr != null && resultStr.equals("succuss"));

			// message
			if (jreader.pathExist("message")) {
				result.setMessage(jreader.readString("message"));
			}
			if (result.getResult() != true) {
				if (StringUtils.isEmpty(result.getMessage()))
					result.setMessage(resultJson);
			}

			// data
			if (type != DummyType.class && jreader.pathExist("body." + respKey))
				result.setBody(jreader.readObjectArray("body." + respKey, type));
		} catch (Exception e) {
			result.setBody(null);
			result.setMessage("invalid json result '" + resultJson + "'");
			result.setResult(false);
		}
		return result;
	}

	public String Post(String path, String content) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(accountServerProperties.getDomain()).append(path).append(content);
		byte[] keys = SHA1RSASignature.compute(accountServerProperties.getCertificate(), sb.toString().getBytes());

		String jsonResult = null;
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(accountServerProperties.getDomain() + path);
			// 解决中文乱码问题
			StringEntity entity = new StringEntity(content, "utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httppost.setEntity(entity);
			httppost.addHeader("Birdex-Client", accountServerProperties.getApp());
			httppost.addHeader("Birdex-Checksum", Hex.encodeHexString(keys));

			CloseableHttpResponse response;
			response = httpClient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				/** 读取服务器返回过来的json字符串数据 **/
				jsonResult = EntityUtils.toString(response.getEntity());
			} else {
				String error = "";
				try {
					error = HttpStatus.valueOf(statusCode).getReasonPhrase();
				} catch (Exception e) {
					error = "Invalid http code '" + statusCode + "'";
				}
				throw new Exception(error);
			}
		} catch (Exception e) {
			throw e;
		}
		return jsonResult;
	}

}
