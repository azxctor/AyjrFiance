package com.hengxin.platform.fund.enums;

import com.hengxin.platform.common.enums.PageEnum;

/**
 * 子账户类型
 * @author dcliu
 *
 */
public enum ESubAcctType implements PageEnum {

	CURRENT("01","活期子账户"),
	LOAN("02","融资子账户"),
	PLEDGE("03","质押子账户"),
	XWB("04","小微宝子账户")
	;
    
	private String code;

    private String text;
	
	private ESubAcctType(String code, String text){
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
