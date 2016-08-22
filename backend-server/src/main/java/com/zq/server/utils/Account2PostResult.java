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
@Data
public class Account2PostResult<T> {
    private Boolean result;
    private String message;
    private Collection<T> body;
}
