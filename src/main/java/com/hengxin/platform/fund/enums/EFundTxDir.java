package com.hengxin.platform.fund.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;


/**
 * 转账方向
 * @author jishen
 *
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum EFundTxDir implements PageEnum{
	
	BANK2EXCHANGE("1","银行转交易所"),
	EXCHANGE2BANK("2","交易所转银行")
	;

	private String code;

    private String text;
	
	private EFundTxDir(String code, String text){
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
