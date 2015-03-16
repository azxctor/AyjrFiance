/**
 * Copyright ArgusHealth. All rights reserved.
 */
package com.hengxin.platform.report.render;

import java.util.List;

import org.w3c.dom.css.CSSPrimitiveValue;
import org.xhtmlrenderer.css.constants.IdentValue;
import org.xhtmlrenderer.css.extend.ContentFunction;
import org.xhtmlrenderer.css.parser.FSFunction;
import org.xhtmlrenderer.css.parser.PropertyValue;
import org.xhtmlrenderer.layout.CounterFunction;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.InlineText;
import org.xhtmlrenderer.render.RenderingContext;

/**
 * This class was borrowed from Flying Saucer with an initial pages counter fix.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
class CustomizedPagesCounterFunction
	implements ContentFunction {

	private static final String COUNTER = "counter";
	private int presetPageCount;

	/**
	 * Constructs a new instance.
	 */
	public CustomizedPagesCounterFunction() {
		this(ITextRendererWrapper.PAGES_COUNTER_NOT_DEFINED);
	}

	/**
	 * Constructs a new instance with the specified integer pagesCount.
	 * 
	 * @param pagesCount
	 *            page count
	 */
	public CustomizedPagesCounterFunction(int pagesCount) {
		presetPageCount = pagesCount;
	}

	/**
	 * reset page count.
	 * 
	 * @param counter
	 *            page count
	 */
	public void reset(int counter) {
		presetPageCount = counter;
	}

	public String calculate(RenderingContext c, FSFunction function,
			InlineText text) {
		if (presetPageCount == ITextRendererWrapper.PAGES_COUNTER_NOT_DEFINED) {
			int value = c.getRootLayer().getRelativePageCount(c);
			return CounterFunction.createCounterText(
					getListStyleType(function), value);
		}
		return String.valueOf(presetPageCount).intern();
	}

	/**
	 * @return canHandle
	 */
	public boolean canHandle(LayoutContext c, FSFunction function) {
		return c.isPrint() && isCounter(function, "pages");
	}

	/**
	 * Whether or not the function value can change at render time. If true,
	 * {@link #calculate(LayoutContext, String, TextContent)} will be called. If
	 * false, {@link #calculate(RenderingContext, String, TextContent)} will be
	 * called.
	 */
	public boolean isStatic() {
		return false;
	}

	public String calculate(LayoutContext c, FSFunction function) {
		return null;
	}

	/**
	 * If a function value can change at render time (i.e. {@link #isStatic()} returns false) use this text as an
	 * approximation at layout.
	 */
	public String getLayoutReplacementText() {
		return "999";
	}

	protected IdentValue getListStyleType(FSFunction function) {
		IdentValue result = IdentValue.DECIMAL;

		List parameters = function.getParameters();
		if (parameters.size() == 2) {
			PropertyValue pValue = (PropertyValue) parameters.get(1);
			IdentValue iValue = IdentValue.valueOf(pValue.getStringValue());
			if (iValue != null) {
				result = iValue;
			}
		}

		return result;
	}

	/**
	 * @return isCounter
	 */
	protected boolean isCounter(FSFunction function, String counterName) {
		if (function.getName().equals(COUNTER)) {
			List parameters = function.getParameters();
			if (parameters.size() == 1 || parameters.size() == 2) {
				PropertyValue param = (PropertyValue) parameters.get(0);
				if (param.getPrimitiveType() != CSSPrimitiveValue.CSS_IDENT
						|| !param.getStringValue().equals(counterName)) {
					return false;
				}

				if (parameters.size() == 2) {
					param = (PropertyValue) parameters.get(1);
					if (param.getPrimitiveType() != CSSPrimitiveValue.CSS_IDENT) {
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}
}
