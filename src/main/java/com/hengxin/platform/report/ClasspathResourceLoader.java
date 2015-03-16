package com.hengxin.platform.report;

import java.io.InputStream;
import java.net.URL;

/**
 * Resource resolver which makes use of ClassLoader.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public final class ClasspathResourceLoader {

	private ClasspathResourceLoader() {
	}

	/**
	 * Loads resource as InputStream.
	 * 
	 * @param resource
	 *            resource name
	 * @return InputStream for the resource, or null
	 */
	public static InputStream getResourceAsStream(String resource) {
		if (resource == null) {
			return null;
		}
		InputStream is = ClasspathResourceLoader.class.getClassLoader().getResourceAsStream(resource);
		if (is != null) {
			return is;
		}
		return ClasspathResourceLoader.class.getResourceAsStream(resource);
	}

	/**
	 * Translates resource name into URL.
	 * 
	 * @param name
	 *            resource name
	 * @return an URL object for the resource, or null
	 */
	public static URL getResource(String name) {
		if (name == null) {
			return null;
		}
		URL url = ClasspathResourceLoader.class.getClassLoader().getResource(name);
		if (url != null) {
			return url;
		}
		return ClasspathResourceLoader.class.getResource(name);
	}
}
