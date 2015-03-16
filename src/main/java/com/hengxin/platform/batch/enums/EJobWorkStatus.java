package com.hengxin.platform.batch.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EJobWorkStatus implements PageEnum {

	READY("R","准备就绪"),
	EXECUTING("E","正在执行"),
	SUCCEED("S","执行成功"),
	FAILED("F","执行失败");
	
	private String code;

    private String text;
	
	private EJobWorkStatus(String code, String text){
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
