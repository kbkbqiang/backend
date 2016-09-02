package com.backend.dao.separate;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * aop 拦截 进行切换数据源
 * 如果service层 增加了@Transactional ，导致数据源MyAbstractRoutingDataSource的determineCurrentLookupKey()方法会在执行DataSourceAop拦截之前就进行全局事务绑定
 * 从而导致获取 DataSourceContextHolder.getJdbcType(); 一直都是空值
 * @Order(-10) 用order来区分
 * @ClassName: DataSourceAspect 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年9月2日 上午11:27:10
 */
@Aspect
@Order(-10)
// 保证该AOP在@Transactional之前执行
@Component
public class DataSourceAspect {

	@Pointcut("execution(* com..service.*.*(..))")
	// @Pointcut("execution(* com..*Service.*(..))")
	public void pointCut() {
	};

	@Before(value = "pointCut()")
	public void before(JoinPoint point) {
		System.out.println("-----pointCut----");
		Object target = point.getTarget();
		String method = point.getSignature().getName();
		Class<?> classz = target.getClass();
		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
		try {
			Method m = classz.getMethod(method, parameterTypes);
			if (m != null && m.isAnnotationPresent(DataSource.class)) {
				DataSource data = m.getAnnotation(DataSource.class);
				HandleDataSource.putDataSource(data.value());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@After("@annotation(targetDataSource)")
//	public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
//		System.out.println("Revert DataSource : {} > {}" + targetDataSource.value() + point.getSignature());
//		// 方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
//		HandleDataSource.reset();
//	}

	/**
	 * 判断是否只读方法
	 * 
	 * @param method
	 *            执行方法
	 * @return 当前方法是否只读
	 */
	private boolean isChoiceReadDB(Method method) {
		Transactional transactionalAnno = AnnotationUtils.findAnnotation(method, Transactional.class);
		if (transactionalAnno == null) {
			return true;
		}
		// 如果之前选择了写库，则现在还选择写库
		if (HandleDataSource.isChoiceWrite()) {
			return false;
		}
		if (transactionalAnno.readOnly()) {
			return true;
		}
		return false;
	}
}
