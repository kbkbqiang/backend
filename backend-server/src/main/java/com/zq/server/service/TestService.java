package com.zq.server.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.backend.dao.separate.DataSource;

/** 
 * @ClassName: TestService 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月23日 下午2:08:58 
 */
@Component
public class TestService {
	
	@DataSource("read")
	public void say(){
		System.out.println("===============");
	}

}
