package com.hengxin.platform.fund.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * 交易发起方
 * @author jishen
 *
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum EFundTxOpt implements PageEnum{

	EXCHANGE("0","交易所"),
	BANK("1","银行")
	;
	
	private String code;

    private String text;
	
	private EFundTxOpt(String code, String text){
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
