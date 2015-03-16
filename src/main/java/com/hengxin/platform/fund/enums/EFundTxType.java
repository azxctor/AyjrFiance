package com.hengxin.platform.fund.enums;

import com.hengxin.platform.common.enums.PageEnum;

/**
 * 货币代码
 */
public enum EFundTxType implements PageEnum {

	OPEN("5100","银行发起开通银商转账服务"),
	ACTIVE("5104","银行发起激活银商转账服务"),
	CLOSE("5101","银行发起关闭银商转账服务"),
    ASSIGN("6100","交易所发起指定银商转账银行")
	;

	private String code;

    private String text;
	
	private EFundTxType(String code, String text){
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
