package com.hengxin.platform.security.authc.session;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * Customized {@link org.apache.shiro.spring.web.ShiroFilterFactoryBean} to allow custom native session cookie name.
 * 
 * @author yeliangjin
 *
 */
public class ShiroFilterFactoryBean extends
		org.apache.shiro.spring.web.ShiroFilterFactoryBean {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShiroFilterFactoryBean.class);

	protected WebSecurityManager createDefaultSecurityManager() {
		return new DefaultWebSecurityManager();
	}

	protected AbstractShiroFilter createInstance() throws Exception {

		LOGGER.debug("Creating Shiro Filter instance.");

		SecurityManager securityManager = getSecurityManager();
		if (securityManager == null) {
			String msg = "SecurityManager property must be set.";
			throw new BeanInitializationException(msg);
		}

		if (!(securityManager instanceof WebSecurityManager)) {
			String msg = "The security manager does not implement the WebSecurityManager interface.";
			throw new BeanInitializationException(msg);
		}

		FilterChainManager manager = createFilterChainManager();

		// Expose the constructed FilterChainManager by first wrapping it in a
		// FilterChainResolver implementation. The AbstractShiroFilter
		// implementations
		// do not know about FilterChainManagers - only resolvers:
		PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
		chainResolver.setFilterChainManager(manager);

		// Now create a concrete ShiroFilter instance and apply the acquired
		// SecurityManager and built
		// FilterChainResolver. It doesn't matter that the instance is an
		// anonymous inner class
		// here - we're just using it because it is a concrete
		// AbstractShiroFilter instance that accepts
		// injection of the SecurityManager and FilterChainResolver:
		return new SpringShiroFilter((WebSecurityManager) securityManager,
				chainResolver);
	}

	private static final class SpringShiroFilter extends AbstractShiroFilter {

		protected SpringShiroFilter(WebSecurityManager webSecurityManager,
				FilterChainResolver resolver) {
			super();
			if (webSecurityManager == null) {
				throw new IllegalArgumentException(
						"WebSecurityManager property cannot be null.");
			}
			setSecurityManager(webSecurityManager);
			if (resolver != null) {
				setFilterChainResolver(resolver);
			}
		}

		protected WebSecurityManager createDefaultSecurityManager() {
			return new ShiroWebSecurityManager();
		}

		protected ServletRequest wrapServletRequest(HttpServletRequest orig) {
			return new ShiroHttpServletRequest(orig, getServletContext(),
					isHttpSessions());
		}
	}

}
