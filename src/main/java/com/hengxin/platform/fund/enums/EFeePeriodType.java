package com.hengxin.platform.fund.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EFeePeriodType implements PageEnum {

	YEAR("Y","年"),
	MONTH("M","月"),
	DAY("D","日");
	
	private String code;

    private String text;
	
	private EFeePeriodType(String code, String text){
		this.code = code;
		this.text = text;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}
}
