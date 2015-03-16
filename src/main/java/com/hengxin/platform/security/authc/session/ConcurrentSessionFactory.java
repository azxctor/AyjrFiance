package com.hengxin.platform.security.authc.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

public class ConcurrentSessionFactory implements SessionFactory {

	public Session createSession(SessionContext initData) {
		if (initData != null) {
			String host = initData.getHost();
			if (host != null) {
				return new ConcurrentSession(host);
			}
		}
		return new ConcurrentSession();
	}

}
