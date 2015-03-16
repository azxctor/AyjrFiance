package com.hengxin.platform.report;

/**
 * Reporting specific exception.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public class ReportingException extends Exception {

	private static final long serialVersionUID = 5L;

	private final ReportContext ctx;

	/**
	 * Construct an exception with given criteria and cause.
	 * 
	 * @param criteria
	 * @param throwable
	 */
	public ReportingException(ReportContext ctx, Throwable throwable) {
		super(throwable);
		this.ctx = ctx;
	}
	
	public ReportingException(Throwable throwable) {
		super(throwable);
		ctx = null;
	}
	
	public ReportingException(String message) {
		super(message);
		ctx = null;
	}
	
	public ReportingException(String message, Throwable throwable) {
		super(message, throwable);
		ctx = null;
	}

	/**
	 * Gets associated criteria if any.
	 * 
	 * @return the criteria
	 */
	public ReportContext getReportContext() {
		return ctx;
	}
}
