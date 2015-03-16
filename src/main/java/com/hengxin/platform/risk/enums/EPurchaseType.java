package com.hengxin.platform.risk.enums;

public enum EPurchaseType {

	PURCHASE("Y"),
	PURCHASE_WITH_WARNING("W"),
	PURCHASE_FAILED("N");
	
	private String code;

	private EPurchaseType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
