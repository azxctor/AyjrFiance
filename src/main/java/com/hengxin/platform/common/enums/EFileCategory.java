package com.hengxin.platform.common.enums;

public enum EFileCategory implements DBEnum {
    BASIC("1000525025", "Basic_Info"), INVESTOR("1000782574", "Investor_Info"), FINANCIER("1000921469",
            "Financier_Info");

	EFileCategory(String code, String text) {
		this.code = code;
		this.text = text;
	}

	private String code;

	private String text;

	@Override
    public String getCode() {
		return code;
	}

	@Override
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
