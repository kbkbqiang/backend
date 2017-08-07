package com.zq.server.utils;

import lombok.Data;

/**
 * 
 * @ClassName: AccountPostResult 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:30:47
 */
public class AccountPostResult<T> {
    private Boolean result;
    private String message;
    private T body;
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
    
}
