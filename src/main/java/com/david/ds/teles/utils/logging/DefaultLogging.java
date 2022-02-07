package com.david.ds.teles.utils.logging;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.quarkus.arc.Priority;
import io.quarkus.logging.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.InvocationContext;

@Priority(9999)
@DefaultLogging.Logged
@Interceptor
public class DefaultLogging {

	@AroundInvoke
	public Object console(InvocationContext context) throws Exception {
		try {
			Log.infof("starting %s", context.getMethod());
			Object result = context.proceed();
			Log.infof("ending %s.%s", context.getClass().getName(), context.getMethod());
			return result;
		} catch (Exception e) {
			Log.errorf("failed to execute %s %n [ERROR] %s", context.getMethod(), e);
			throw e;
		}
	}

	@InterceptorBinding
	@Retention(RUNTIME)
	@Target({ TYPE, METHOD })
	public static @interface Logged {
	}
}
