package com.zq.server.utils;

import java.util.Collection;

import lombok.Data;

/**
 * 
 * @ClassName: Account2PostResult 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:30:32
 */
public class Account2PostResult<T> {
    private Boolean result;
    private String message;
    private Collection<T> body;
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
	public Collection<T> getBody() {
		return body;
	}
	public void setBody(Collection<T> body) {
		this.body = body;
	}
    
}
