package com.hengxin.platform.risk.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum ERiskBearLevel implements PageEnum {
	
	ROOKIE("00","新手型"),
	CAUTIOUS("01","谨慎型"),
	STEADY("02","稳健型"),
	AGGRESSIVE("03","进取型");
	
	private String code;

    private String text;
	
	private ERiskBearLevel(String code, String text){
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
