package com.hengxin.platform.escrow.enums;

import com.hengxin.platform.common.enums.PageEnum;

/**
 * 短信模板
 * @author congzhou
 *
 */
public enum EEbcSmsModel implements PageEnum {

	NULL("", ""),
	M("m", "动态密码"),
	M1("m1", "绑定手机的动态验证");
	
	private String code;

    private String text;
	
    EEbcSmsModel(String code, String text) {
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
