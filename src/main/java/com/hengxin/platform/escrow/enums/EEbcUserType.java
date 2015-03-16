package com.hengxin.platform.escrow.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EEbcUserType implements PageEnum {

	NULL("", ""),
	PERSON("0", "个人"),
	ENTERPRISE("1", "企业");

	
	private String code;

    private String text;
	
    EEbcUserType(String code, String text) {
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
