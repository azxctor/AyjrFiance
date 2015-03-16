package com.hengxin.platform.fund.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * 综合账户类型
 * @author dcliu
 *
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum EAcctStatus implements PageEnum {

	NORMAL("01","正常"),
	RECNOPAY("02","只收不付");
	
	private String code;

    private String text;
	
	private EAcctStatus(String code, String text){
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
