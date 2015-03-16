package com.hengxin.platform.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Utility to access Spring context from a non-managed bean.
 * 
 * @author yeliangjin
 * 
 */
@Component
public final class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static <T> T getBean(Class<T> clz) {
		return applicationContext.getBean(clz);
	}

	public static <T> T getBean(String name, Class<T> clz) {
		return applicationContext.getBean(name, clz);
	}

	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) {
		ApplicationContextUtil.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}