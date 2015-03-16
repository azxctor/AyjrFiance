/**
 * Copyright ArgusHealth. All rights reserved.
 */
package com.hengxin.platform.report;

/**
 * Exception that will be thrown when a report cannot rendered based on
 * templates.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
class RenderingException
	extends Exception {

	private static final long serialVersionUID = 5L;

	/**
	 * @see Exception#Exception(String)
	 */
	public RenderingException(String msg) {
		super(msg);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public RenderingException(Throwable cause) {
		super(cause);
	}
}
