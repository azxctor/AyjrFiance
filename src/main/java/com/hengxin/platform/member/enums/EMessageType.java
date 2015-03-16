package com.hengxin.platform.member.enums;

public enum EMessageType {

    NULL(""), MESSAGE("M"), TODO("T"), SMS("S");

    private String code;

    EMessageType(String code) {
        this.code = code;
    }

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
    
}
