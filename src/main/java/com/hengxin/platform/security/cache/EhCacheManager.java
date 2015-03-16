package com.hengxin.platform.security.cache;

public class EhCacheManager extends
		org.apache.shiro.cache.ehcache.EhCacheManager {

	public EhCacheManager() {
		super();
		if (System.getProperty("ehcache.disk.store.dir") == null
				&& System.getProperty("jboss.server.temp.dir") != null) {
			System.setProperty("ehcache.disk.store.dir",
					System.getProperty("jboss.server.temp.dir"));
		}
	}
}
