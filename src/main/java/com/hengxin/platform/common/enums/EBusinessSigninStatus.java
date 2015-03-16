package com.hengxin.platform.common.enums;

public enum EBusinessSigninStatus implements DBEnum {
	OPEN("O", "Open"), CLOSE("C", "Close");

	EBusinessSigninStatus(String code, String text) {
		this.code = code;
		this.text = text;
	}

	private String code;

	private String text;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
