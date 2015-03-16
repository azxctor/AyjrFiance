/**
 * Copyright ArgusHealth. All rights reserved.
 */
package com.hengxin.platform.report;

/**
 * Supporting class which is used for huge PDF. This class is intended to be
 * used within the library.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
final class NullReportOutput
	implements ReportOutput {
	public static final NullReportOutput INSTANCE = new NullReportOutput();

	private NullReportOutput() {
		// prevent instantiating from outside
	}

	/**
	 * Checks if given output is this one.
	 * 
	 * @param output
	 * @return true if it's the same instance
	 */
	public static boolean isNull(ReportOutput output) {
		return INSTANCE.equals(output);
	}

	/**
	 * @see ReportOutput#getFileName()
	 */
	@Override
	public String getFileName() {
		return null;
	}
}
