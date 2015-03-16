/**
 * Copyright ArgusHealth. All rights reserved.
 */
package com.hengxin.platform.report;

/**
 * Exception that will be thrown during template execution.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
class TemplateExecutionException
	extends Exception {

	private static final long serialVersionUID = 5L;

	/**
	 * @see Exception#Exception(String)
	 */
	public TemplateExecutionException(String msg) {
		super(msg);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public TemplateExecutionException(Throwable cause) {
		super(cause);
	}
}
