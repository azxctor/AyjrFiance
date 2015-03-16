package com.hengxin.platform.escrow.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EEbcBankQueryType implements PageEnum {

	NULL("", ""),
	PAYEEBANK("1", "大行"),
	PROVINCE("2", "省份"),
	CITY("3", "城市"),
	BANK("4", "支行");
	
	private String code;

    private String text;
	
    EEbcBankQueryType(String code, String text) {
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
