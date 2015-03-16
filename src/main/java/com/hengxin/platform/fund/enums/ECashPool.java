package com.hengxin.platform.fund.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * 资金池类型
 * @author dcliu
 *
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum ECashPool implements PageEnum {
	
	ALL("ALL", "全部"),
	CMB_COMMON("01","招商银行(普通)"),
	CMB_SPECIAL("02","招商银行(专用)"),
	ICBC_COMMON("03","工商银行(普通)"),
	HXB_COMMON("06","华夏银行(专用)"),
	ESCROW_EBC("EBC","银盈通托管")
	;
	
	
	private String code;

    private String text;
	
	private ECashPool(String code, String text){
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
