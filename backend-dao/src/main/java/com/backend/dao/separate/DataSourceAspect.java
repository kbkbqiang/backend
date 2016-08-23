package com.backend.dao.separate;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @ClassName: DataSourceAspect
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author seven
 * @date 2014-10-14 上午9:24:23
 * 
 */
@Aspect
@Component
public class DataSourceAspect {
	@Pointcut("execution(* com..service.*.*(..))")
//	@Pointcut("execution(* com..*Service.*(..))")
	public void pointCut() {
	};

	@Before(value = "pointCut()")
	public void before(JoinPoint point) {
		System.out.println("-----pointCut----");
		Object target = point.getTarget();
		String method = point.getSignature().getName();
		Class<?> classz = target.getClass();
		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
				.getMethod().getParameterTypes();
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
}
