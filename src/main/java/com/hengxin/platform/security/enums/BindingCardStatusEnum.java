package com.hengxin.platform.security.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum BindingCardStatusEnum implements PageEnum {
	
	UNDO("0","未绑卡"),
	PROCESS("1","绑卡中"),
	DONE("2","已绑卡");

    private String code;

    private String text;

    BindingCardStatusEnum(String code, String text) {
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
