package com.hengxin.platform.security.authc.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This is a wrapper of
 * {@link org.apache.shiro.web.servlet.ShiroHttpServletRequest} to allow
 * customization of the {@link #getSession(boolean)} method.
 * 
 * @author yeliangjin
 * 
 */
public class ShiroHttpServletRequest extends
		org.apache.shiro.web.servlet.ShiroHttpServletRequest {

	private HttpServletRequest rawRequest;

	public ShiroHttpServletRequest(HttpServletRequest wrapped,
			ServletContext servletContext, boolean httpSessions) {
		super(wrapped, servletContext, httpSessions);
		rawRequest = wrapped;
	}

	@Override
	public HttpSession getSession(boolean create) {
		if (!isHttpSessions()) {
			rawRequest.getSession(create);
		}
		return super.getSession(create);
	}
}
