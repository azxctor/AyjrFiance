package com.hengxin.platform.product.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EProductStatus implements PageEnum {

    NULL("", ""), STANDBY("01", "待提交"), WAITTOAPPROVE("02", "待审核"), WAITTOEVALUATE("03", "待评级"), WAITTOFREEZE("04",
            "待冻结"), WAITTOPUBLISH("05", "待发布"), FINISHED("06", "发布完成"), ABANDONED("07", "项目废弃");

	private String code;

	private String text;

	EProductStatus(String code, String text) {
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
