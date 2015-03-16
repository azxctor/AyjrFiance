package com.hengxin.platform.fund.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EFeeType implements PageEnum {

	SEAT("01","坐席服务费"),
	RISKMANAGE("02","风险管理费"),
	LOANSERVE("03","融资服务费");
	
	private String code;

    private String text;
	
	private EFeeType(String code, String text){
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
