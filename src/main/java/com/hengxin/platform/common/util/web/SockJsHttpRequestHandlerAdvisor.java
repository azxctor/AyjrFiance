package com.hengxin.platform.common.util.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler;

/**
 * To workaround the port=-1 issue that happens when using Apache as reverse proxy.
 * 
 * @author yeliangjin
 *
 */
public class SockJsHttpRequestHandlerAdvisor extends
		StaticMethodMatcherPointcutAdvisor {

	private static final long serialVersionUID = 1L;

	public SockJsHttpRequestHandlerAdvisor() {
		setAdvice(new PortSafeRequestInterceptor());
	}

	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return targetClass == SockJsHttpRequestHandler.class
				&& "handleRequest".equals(method.getName());
	}

	static class PortSafeRequestInterceptor implements MethodInterceptor {

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			Object[] args = invocation.getArguments();
			args[0] = new PortSafeHttpServletRequest(
					(HttpServletRequest) args[0]);
			return invocation.proceed();
		}
	}

	static class PortSafeHttpServletRequest extends HttpServletRequestWrapper {

		public PortSafeHttpServletRequest(HttpServletRequest request) {
			super(request);
		}

		@Override
		public int getRemotePort() {
			int port = super.getRemotePort();
			return port < 0 ? 0 : port;
		}

	}

}
