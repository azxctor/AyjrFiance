package com.hengxin.platform.fund.enums;

import com.hengxin.platform.common.enums.PageEnum;

/**
 * 货币代码
 */
public enum EAgrmntStatus implements PageEnum {

	NORMAL("0","正常"),
	CLOSED("1","销户")
	;

	private String code;

    private String text;
	
	private EAgrmntStatus(String code, String text){
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
