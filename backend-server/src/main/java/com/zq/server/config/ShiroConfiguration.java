package com.zq.server.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.zq.server.filter.shiro.StatelessAuthcFilter;
import com.zq.server.filter.shiro.StatelessDefaultSubjectFactory;
import com.zq.server.filter.shiro.StatelessRealm;

/**
 * @ClassName: ShiroConfiguration
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午10:26:46
 */
@Configuration
@Slf4j
public class ShiroConfiguration {

	/**
	 * 注册DelegatingFilterProxy（Shiro） 集成Shiro有2种方法： 1.
	 * 按这个方法自己组装一个FilterRegistrationBean（这种方法更为灵活，可以自己定义UrlPattern，
	 * 在项目使用中你可能会因为一些很但疼的问题最后采用它， 想使用它你可能需要看官网或者已经很了解Shiro的处理原理了） 2.
	 * 直接使用ShiroFilterFactoryBean
	 * （这种方法比较简单，其内部对ShiroFilter做了组装工作，无法自己定义UrlPattern， 默认拦截 /*）
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean delegatingFilterRegistration() {
		DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
		delegatingFilterProxy.setTargetBeanName("shiroFilter");
		delegatingFilterProxy.setTargetFilterLifecycle(true);
		
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(delegatingFilterProxy);
		registrationBean.setUrlPatterns(Arrays.asList("/*"));
		registrationBean.setOrder(1);
		
		return registrationBean;
	}

	@Bean(name = "realm")
	@DependsOn("lifecycleBeanPostProcessor")
	public StatelessRealm realm() {
		final StatelessRealm realm = new StatelessRealm();
		realm.setCachingEnabled(false);
		return realm;
	}

	@Bean(name = "subjectFactory")
	public DefaultWebSubjectFactory subjectFactory() {
		return new StatelessDefaultSubjectFactory();
	}

	@Bean
	public DefaultWebSessionManager sessionManager() {
		final DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionValidationSchedulerEnabled(false);
		return sessionManager;
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager() {
		final DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(realm());
		SubjectDAO dao = securityManager.getSubjectDAO();
		if (dao != null && dao instanceof DefaultSubjectDAO) {
			SessionStorageEvaluator eva = ((DefaultSubjectDAO) dao).getSessionStorageEvaluator();
			if (eva != null && eva instanceof DefaultWebSessionStorageEvaluator) {
				((DefaultWebSessionStorageEvaluator) eva).setSessionStorageEnabled(false);
			}
		}
		securityManager.setSubjectFactory(subjectFactory());
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	@Bean(name = "statelessAuthcFilter")
	public StatelessAuthcFilter statelessAuthcFilter() {
		return new StatelessAuthcFilter();
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager());

		Map<String, String> filterChainDefinitionMapping = new HashMap<String, String>();
		filterChainDefinitionMapping.put("/**", "statelessAuthc");
		factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMapping);

		Map<String, Filter> filters = new HashMap<String, Filter>();
		filters.put("statelessAuthc", statelessAuthcFilter());
		factoryBean.setFilters(filters);

		// factoryBean.getFilters().put("statelessAuthc",statelessAuthcFilter());
		// factoryBean.getFilterChainDefinitionMap().put("/**","statelessAuthc");
		return factoryBean;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean() {
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		methodInvokingFactoryBean.setArguments(new Object[] { securityManager() });

		return methodInvokingFactoryBean;
	}

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager());
		return aasa;
	}

}
