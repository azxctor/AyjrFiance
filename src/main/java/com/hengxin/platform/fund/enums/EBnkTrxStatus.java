package com.hengxin.platform.fund.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EBnkTrxStatus implements PageEnum {

	NORMAL("N","正常"),
	DEALING("D","处理中"),
	REVERSED("R","已冲正"),
	
	ALL("ALL","全部");
	
	private String code;

    private String text;
	
	private EBnkTrxStatus(String code, String text){
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
