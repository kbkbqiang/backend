package com.zq.server.utils;

import lombok.Data;

/**
 * 
 * @ClassName: AccountPostResult 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:30:47
 */
@Data
public class AccountPostResult<T> {
    private Boolean result;
    private String message;
    private T body;
}
