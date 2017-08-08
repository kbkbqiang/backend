package com.zq.server.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: ShiroConfiguration
 * @Description TODO
 * @author zhaoqiang
 * @date: 2016年8月22日 上午10:26:46
 */
@Configuration
@Slf4j
public class ShiroConfiguration {
	
	private static Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

	/**
	 * 注册DelegatingFilterProxy（Shiro） 集成Shiro有2种方法： 
	 * 1.按这个方法自己组装一个FilterRegistrationBean（这种方法更为灵活，可以自己定义UrlPattern，
	 * 在项目使用中你可能会因为一些很但疼的问题最后采用它， 想使用它你可能需要看官网或者已经很了解Shiro的处理原理了） 
	 * 2.直接使用ShiroFilterFactoryBean
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
		// 设置realm.
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

	/**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     * Filter Chain定义说明 
     * 1、一个URL可以配置多个Filter，使用逗号分隔 
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     *
     */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		factoryBean.setSecurityManager(securityManager());
		
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		factoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
		factoryBean.setSuccessUrl("/index");
        // 未授权界面;
		factoryBean.setUnauthorizedUrl("/403");

		// 拦截器.
		Map<String, String> filterChainDefinitionMapping = new HashMap<String, String>();
		// 配置不会被拦截的链接 顺序判断
		filterChainDefinitionMapping.put("/static/**", "anon");
		filterChainDefinitionMapping.put("/ajaxLogin", "anon");
		
		// <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		filterChainDefinitionMapping.put("/**", "statelessAuthc");
		factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMapping);

		Map<String, Filter> filters = new HashMap<String, Filter>();
		filters.put("statelessAuthc", statelessAuthcFilter());
		factoryBean.setFilters(filters);

		// factoryBean.getFilters().put("statelessAuthc",statelessAuthcFilter());
		// factoryBean.getFilterChainDefinitionMap().put("/**","statelessAuthc");
		logger.info("Shiro拦截器工厂类注入成功");
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
