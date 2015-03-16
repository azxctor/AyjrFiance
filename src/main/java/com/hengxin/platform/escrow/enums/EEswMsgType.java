package com.hengxin.platform.escrow.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EEswMsgType implements PageEnum {

	NULL("", ""),
	REQ("0", "请求"),
	RESP("1", "响应");
	
	private String code;

    private String text;
	
    EEswMsgType(String code, String text) {
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
