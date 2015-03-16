package com.hengxin.platform.security.authc.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.session.mgt.SimpleSession;

public class ConcurrentSession extends SimpleSession {

	private static final long serialVersionUID = 1L;

	public ConcurrentSession() {
		super();
	}

	public ConcurrentSession(String host) {
		super(host);
	}

	public void setAttribute(Object key, Object value) {
		if (value == null) {
			removeAttribute(key);
		} else {
			getAttributesLazy().put(key, value);
		}
	}

	private Map<Object, Object> getAttributesLazy() {
		Map<Object, Object> attributes = getAttributes();
		if (attributes == null) {
			// use ConcurrentHashMap instead of HashMap
			attributes = new ConcurrentHashMap<Object, Object>();
			setAttributes(attributes);
		}
		return attributes;
	}

}
