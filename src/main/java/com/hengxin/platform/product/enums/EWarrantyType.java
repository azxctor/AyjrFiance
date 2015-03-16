package com.hengxin.platform.product.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EWarrantyType implements PageEnum {

	NULL("", "", ""), PRINCIPAL("A", "本金担保", "1"), PRINCIPALINTEREST("B", "本息担保", "2"), MONITORASSETS(
			"C", "资产监管", "4"), NOTHING("D", "无担保", "3");

	private String code;

	private String text;
	
	private String label;

	EWarrantyType(String code, String text, String label) {
		this.code = code;
		this.text = text;
		this.label = label;
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

	public String getLabel() {
		return label;
	}
	
	
}
