package com.hengxin.platform.report.excel.xml;

import java.util.HashMap;

public class XmlSpreadsheetNode extends HashMap<String, String> {
	private static final long serialVersionUID = 5L;

	private String qName;

	/**
	 * Attributes to string.
	 * 
	 * @return string
	 */
	public String attrsToString() {
		StringBuilder builder = new StringBuilder();
		for (java.util.Map.Entry<String, String> entry : entrySet()) {
			if (builder.length() > 0) {
				builder.append(',');
			}
			builder.append(entry.getKey()).append(':').append(entry.getValue());
		}
		return builder.toString();
	}

	/**
	 * @return the qName
	 */
	public String getqName() {
		return qName;
	}

	/**
	 * @param qName
	 *            the qName to set
	 */
	public void setqName(String qName) {
		this.qName = qName;
	}

}
