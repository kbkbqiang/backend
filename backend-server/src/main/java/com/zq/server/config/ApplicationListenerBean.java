package com.zq.server.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/** 
 * @ClassName: ApplicationListenerBean 
 * @Description TODO 
 * @author zhaoqiang 
 * @date: 2016年8月19日 上午11:44:33 
 */
@Slf4j
public class ApplicationListenerBean implements ApplicationListener<ApplicationEvent> {

	@Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
        	
        }
    }
}
