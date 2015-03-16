/**
 * Copyright ArgusHealth. All rights reserved.
 */
package com.hengxin.platform.report.render;

import java.lang.reflect.Field;
import java.util.List;

import org.xhtmlrenderer.context.ContentFunctionFactory;

/**
 * Replace the orginal pages counter function with the {@link CustomizedPagesCounterFunction}.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
class CustomizedContentFunctionFactory
	extends ContentFunctionFactory {
	private CustomizedPagesCounterFunction pagesCounterFunction;

	CustomizedContentFunctionFactory() {
		super();
		try {
			Field field = ContentFunctionFactory.class.getDeclaredField("_functions");
			field.setAccessible(true);
			List<?> list = (List<?>) field.get(this);
			list.remove(1);
			pagesCounterFunction = new CustomizedPagesCounterFunction();
			registerFunction(pagesCounterFunction);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * preset page count with the integer counter.
	 * 
	 * @param counter
	 *            page count
	 */
	public void setTotalPageCounter(int counter) {
		pagesCounterFunction.reset(counter);
	}
}
