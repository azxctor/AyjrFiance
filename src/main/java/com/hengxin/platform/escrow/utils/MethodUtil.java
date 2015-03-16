package com.hengxin.platform.escrow.utils;

import java.lang.reflect.Method;

import org.apache.commons.beanutils.MethodUtils;

public final class MethodUtil {

	public static Method getGetMethod(Class<?> clz, String fieldName) {
		String firstLetter = fieldName.substring(0, 1).toUpperCase();
		String getMethodName = "get" + firstLetter + fieldName.substring(1);
		return MethodUtils.getAccessibleMethod(clz, getMethodName,
				(Class[]) null);
	}

	public static Method getSetMethod(Class<?> clz, String fieldName) {
		String firstLetter = fieldName.substring(0, 1).toUpperCase();
		String setMethodName = "set" + firstLetter + fieldName.substring(1);
		return MethodUtils
				.getAccessibleMethod(clz, setMethodName, String.class);
	}

}
