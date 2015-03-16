package com.hengxin.platform.fund.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * 交易类型
 * @author congzhou
 *
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum EFundTrdType implements PageEnum {

	BONDSUBS("01","债权申购"),
	BONDASSIGN("02","债权转让"),
	ALL("99","全部");
	
	private String code;

    private String text;
	
	private EFundTrdType(String code, String text){
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
