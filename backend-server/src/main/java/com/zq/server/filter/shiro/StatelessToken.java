package com.zq.server.filter.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 
 * 
 * @ClassName: StatelessToken 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 上午10:50:34
 */
public class StatelessToken implements AuthenticationToken {

    private String username;
    private String clientDigest;

    public StatelessToken(String username, String clientDigest) {
        this.username = username;
        this.clientDigest = clientDigest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientDigest() {
        return clientDigest;
    }

    public void setClientDigest(String clientDigest) {
        this.clientDigest = clientDigest;
    }

    public Object getPrincipal() {
        return username;
    }

    public Object getCredentials() {
        return clientDigest;
    }
}
