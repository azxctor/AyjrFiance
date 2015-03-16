package com.hengxin.platform.security.authc.session;

import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Customized {@link DefaultWebSecurityManager} to allow custom native session cookie name.
 * 
 * @author yeliangjin
 *
 */
public class ShiroWebSecurityManager extends DefaultWebSecurityManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShiroWebSecurityManager.class);

	protected SessionManager createSessionManager(String sessionMode) {
		if (sessionMode == null
				|| !sessionMode.equalsIgnoreCase(NATIVE_SESSION_MODE)) {
			LOGGER.info(
					"{} mode - enabling ServletContainerSessionManager (HTTP-only Sessions)",
					HTTP_SESSION_MODE);
			return new ServletContainerSessionManager();
		} else {
			LOGGER.info(
					"{} mode - enabling DefaultWebSessionManager (non-HTTP and HTTP Sessions)",
					NATIVE_SESSION_MODE);
			return new ShiroWebSessionManager();
		}
	}

}
