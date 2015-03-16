package com.hengxin.platform.batch.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum ETaskGroupId implements PageEnum {
	
	REPAYMENT("REPAYMENT","批量还款"),
	BIZTASK("BIZTASK","日终批量"),
	ROLLDATE("ROLLDATE","系统日切");
	
	private String code;
    private String text;
	
	private ETaskGroupId(String code, String text){
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
