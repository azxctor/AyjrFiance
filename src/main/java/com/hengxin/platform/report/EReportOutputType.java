package com.hengxin.platform.report;

/**
 * Possible formats of report.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public enum EReportOutputType {
	Null("", ""), PDF("PDF", "P"), Delimited("Delimited", "C"), Excel("Excel", "E"), Xml("XML", "X"), Html("HTML", "H");
	private String label;

	private String code;

	private EReportOutputType(String lab, String c) {
		label = lab;
		code = c;
	}

	@Override
	public String toString() {
		return label;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
}
