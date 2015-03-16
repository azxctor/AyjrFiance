package com.hengxin.platform.escrow.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EOrderStatusEnum implements PageEnum {
	
	WAITING("W","待处理"),
	PROCESS("P","处理中"),
	SUCCESS("S","成功"),
	FAILED("F","失败"),
	UNKNOW("U","未知"),
	ABANDON("A","作废"),
	ALL("ALL","全部");// 下拉框用到

	private String code;

    private String text;
	
	private EOrderStatusEnum(String code, String text){
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
