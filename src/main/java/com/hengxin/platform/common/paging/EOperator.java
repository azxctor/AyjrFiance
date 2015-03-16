package com.hengxin.platform.common.paging;

/**
 * 
 * @author Gregory
 */
public enum EOperator {

	EQ(" = ", "eq"), 
	NE(" != ", "ne"),
	GREAT_THAN(" > ", "gt"),
	GREAT_EQ(" >= ", "ge"),
	LESS_THAN(" < ", "lt"),
	LESS_EQ(" <= ", "le"),
	BETWEEN(" BTW ", "btw"),
	LIKE("%", "like");

	private String code;
	private String label;
	
	/**
	 * @param value
	 *            String
	 * @param code
	 *            String
	 */
	EOperator(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

}

