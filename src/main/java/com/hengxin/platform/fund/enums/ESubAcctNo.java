package com.hengxin.platform.fund.enums;

import com.hengxin.platform.common.enums.PageEnum;

/**
 * 综合账户子账户编号
 * @author dcliu
 *
 */
public enum ESubAcctNo implements PageEnum {

	CURRENT("000001","活期子账户"),
	LOAN("000002","融资子账户"),
	PLEDGE("000003","质押子账户"),
	XWB("000004","小微宝子账户")
	;
    
	private String code;

    private String text;
	
	private ESubAcctNo(String code, String text){
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
