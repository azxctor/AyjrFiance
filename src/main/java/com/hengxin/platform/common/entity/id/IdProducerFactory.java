package com.hengxin.platform.common.entity.id;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * Factory class to retrieve appropriate {@link IdProducer}.
 * 
 * @author yeliangjin
 * 
 */
class IdProducerFactory {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(IdProducerFactory.class);

	private static final String BASE_PACKAGE = IdProducer.class.getPackage()
			.getName();
	private static Map<Type, IdProducer<?>> map = new HashMap<Type, IdProducer<?>>();

	static {
		loadProducers();
	}

	/**
	 * Factory method.
	 */
	@SuppressWarnings("unchecked")
	public static <T> IdProducer<T> getProducer(Class<T> pojoType) {
		IdProducer<?> producer = map.get(pojoType);
		if (producer == null) {
			producer = map.get(Object.class);
		}
		return (IdProducer<T>) producer;
	}

	private static void loadProducers() {
		for (Class<?> c : findTypes(BASE_PACKAGE, IdProducer.class)) {
			ParameterizedType pt = null;
			Type[] ipt = c.getGenericInterfaces();
			if (ipt.length > 0) {
				pt = (ParameterizedType) c.getGenericInterfaces()[0];
			} else {
				pt = (ParameterizedType) c.getGenericSuperclass();
			}
			Type entityType = pt.getActualTypeArguments()[0];
			Type mappedType = entityType instanceof TypeVariable<?> ? Object.class
					: entityType;
			if (map.containsKey(mappedType)) {
				LOGGER.debug("duplicated producer {} for entity {}", c,
						mappedType);
				continue;
			}
			IdProducer<?> instance = (IdProducer<?>) BeanUtils.instantiate(c);
			map.put(mappedType, instance);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(MessageFormat.format("added {0} <-> {1}", c,
						mappedType));
			}
		}
	}

	private static List<Class<?>> findTypes(String basePackage, Class<?> clazz) {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
				resourcePatternResolver);

		List<Class<?>> candidates = new ArrayList<Class<?>>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ resolveBasePackage(basePackage) + "/" + "**/*.class";
		Resource[] resources = {};
		try {
			resources = resourcePatternResolver.getResources(packageSearchPath);
		} catch (IOException e) {
			LOGGER.error("cannot enumerate resources", e);
		}
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				try {
					MetadataReader metadataReader = metadataReaderFactory
							.getMetadataReader(resource);
					if (isCandidate(metadataReader, clazz)) {
						candidates.add(Class.forName(metadataReader
								.getClassMetadata().getClassName()));
					}
				} catch (Exception e) {
					LOGGER.warn("cannot resolve type {}", e);
					continue;
				}
			}
		}
		return candidates;
	}

	private static String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils
				.resolvePlaceholders(basePackage));
	}

	private static boolean isCandidate(MetadataReader metadataReader,
			Class<?> clazz) throws ClassNotFoundException {
		Class<?> c = Class.forName(metadataReader.getClassMetadata()
				.getClassName());
		if (c.isInterface()) {
			return false;
		}
		if (clazz.isAssignableFrom(c)) {
			return true;
		}
		return false;
	}

}
