package com.hengxin.platform.fund.enums;

import com.hengxin.platform.common.enums.PageEnum;

/**
 * 综合账户类型
 * @author dcliu
 *
 */
public enum EAcctType implements PageEnum {

	ASSETS("01","资产类账户"),
	DEBT("02","负债类账户");
	
	private String code;

    private String text;
	
	private EAcctType(String code, String text){
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
