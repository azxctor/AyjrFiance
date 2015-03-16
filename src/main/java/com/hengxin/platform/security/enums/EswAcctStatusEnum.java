package com.hengxin.platform.security.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EswAcctStatusEnum implements PageEnum {
	
	UNDO("0","未开户"),
	DONE("2","已开户");

    private String code;

    private String text;

    EswAcctStatusEnum(String code, String text) {
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
