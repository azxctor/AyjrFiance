package com.hengxin.platform.escrow.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EEbcAbtNoType implements PageEnum {

	ELECACCOUNT("000001","电子账户"),	
	BANKCARD("000002,","银行卡帐户"),	
	PREPAIDCARD("000003","预付卡账户");	
	
	private String code;

    private String text;
	
    EEbcAbtNoType(String code, String text) {
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
