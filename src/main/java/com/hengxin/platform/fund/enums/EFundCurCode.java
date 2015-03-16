package com.hengxin.platform.fund.enums;

import com.hengxin.platform.common.enums.PageEnum;

/**
 * 货币代码
 */
public enum EFundCurCode implements PageEnum {

	CNY("CNY","人民币"),
	HKD("HKD","港币"),
	USD("USD","美元")
	;

	private String code;

    private String text;
	
	private EFundCurCode(String code, String text){
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
