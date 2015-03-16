package com.hengxin.platform.product.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EProductMortgageRType implements PageEnum {

	NULL("", ""), RESIDENCE("01", "住宅"), STORE("02", "商铺"), OFFICEBUILDING(
			"03", "写字楼"), OTHER("04", "其他");

	private String code;

	private String text;

	EProductMortgageRType(String code, String text) {
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
