package com.hengxin.platform.common.entity.id;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hengxin.platform.common.entity.BasePo;
import com.hengxin.platform.member.domain.BaseApplication;

/**
 * Injects identifiers automatically for target entities.
 * 
 * @author yeliangjin
 * 
 */
public class IdInjectionEntityListener {

	private static final Map<Class<?>, Field> ID_FIELD_MAP = new ConcurrentHashMap<Class<?>, Field>();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(IdInjectionEntityListener.class);

	@PrePersist
	public void prePersist(Object entity) {
		injectId(entity);
		if (entity instanceof BaseApplication) {
			BaseApplication e = (BaseApplication) entity;
			e.setCreateTs(new Date());
		} else if (entity instanceof BasePo) {
			BasePo e = (BasePo) entity;
			e.setCreateTs(new Date());
		}
	}

	private static void injectId(Object entity) {
		try {
			if (findId(entity).get(entity) == null) {
				Object id = IdUtil.produce(entity);
				findId(entity).set(entity, id);
			}
		} catch (Exception e) {
			LOGGER.warn("error while injecting id", e);
			throw new RuntimeException("unable to inject id", e);
		}
	}

	private static Field findId(Object entity) {
		Class<?> clz = entity.getClass();
		if (ID_FIELD_MAP.containsKey(clz)) {
			return ID_FIELD_MAP.get(clz);
		}
		while (clz != Object.class) {
			for (Field f : clz.getDeclaredFields()) {
				if (f.isAnnotationPresent(Id.class)) {
					if (f.getType() != String.class) {
						throw new IllegalArgumentException(
								"invalid id type, unable to inject: "
										+ f.getType());
					}
					f.setAccessible(true);
					ID_FIELD_MAP.put(entity.getClass(), f);
					return f;
				}
			}

			clz = clz.getSuperclass();
		}
		throw new IllegalArgumentException(
				"unable to find id field for class: " + entity.getClass());
	}

}
