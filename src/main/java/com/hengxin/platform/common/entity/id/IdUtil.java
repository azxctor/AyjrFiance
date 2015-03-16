package com.hengxin.platform.common.entity.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a centralized interface to produce a system-generated identifier.
 * 
 * @author yeliangjin
 * 
 */
public final class IdUtil {

	private IdUtil() {

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(IdUtil.class);
	private static final String GL_DUMMY = "GL_DUMMY";

	/**
	 * Generates a unique id for "PO" entities.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Object produce(T entity) {
		long start = System.currentTimeMillis();
		IdProducer<T> p = (IdProducer<T>) IdProducerFactory.getProducer(entity
				.getClass());
		Object id = p.produce(new Context(entity));
		LOGGER.info("identifier generated for {} in {}ms: {} ", entity.getClass()
				.getSimpleName(), System.currentTimeMillis() - start, id);
		return id;
	}

	/**
	 * In case a unique id is needed for no specific business purpose.
	 */
	public static String produce() {
		long start = System.currentTimeMillis();
		IdProducer<Object> p = IdProducerFactory.getProducer(Object.class);
		Object id = p.produce(new Context(GL_DUMMY));
		LOGGER.info("dummy identifier generated in {}ms: {}", System.currentTimeMillis() - start, id);
		return id.toString();
	}

}
