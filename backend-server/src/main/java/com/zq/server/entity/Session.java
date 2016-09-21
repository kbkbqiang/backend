package com.zq.server.entity;


/**
 * @ClassName: Session 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午11:32:27
 */
public class Session {
    private String token;

    public Session(String token,String password, Object user) {
        this.token = token;
    }

    public Session(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
