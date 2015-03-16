package com.hengxin.platform.fund.enums;

import com.hengxin.platform.common.enums.PageEnum;

/**
 * 钞汇标志
 */
public enum EFundCurFlag implements PageEnum {


	IRRESPECTIVE("0","不分钞汇"),
	CASH("1","现钞"),
	REMITTANCE("2","现汇")
	;

	private String code;

    private String text;
	
	private EFundCurFlag(String code, String text){
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
