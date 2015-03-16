package com.hengxin.platform.escrow.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum AssetsCategoryEnum implements PageEnum {
	
	ELETC_ACCT("000001","电子账户"),
	BANK_CARD_ACCT("000002","银行卡账户"),
	PREPAY_ACCT("000003","预付卡账户");
	
	private String code;

    private String text;
	
	private AssetsCategoryEnum(String code, String text){
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
