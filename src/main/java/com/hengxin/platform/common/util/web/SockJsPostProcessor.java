package com.hengxin.platform.common.util.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.socket.sockjs.support.AbstractSockJsService;

/**
 * To force an injection on the <code>clientLibraryUrl</code> property as XML
 * based spring-websocket configuration won't read that property.
 * 
 * @author yeliangjin
 * 
 */
public class SockJsPostProcessor implements BeanFactoryPostProcessor {

	private String clientLibraryUrl;

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		AbstractSockJsService service = beanFactory
				.getBean(AbstractSockJsService.class);
		service.setSockJsClientLibraryUrl(clientLibraryUrl);
	}

	public void setClientLibraryUrl(String clientLibraryUrl) {
		this.clientLibraryUrl = clientLibraryUrl;
	}

}
