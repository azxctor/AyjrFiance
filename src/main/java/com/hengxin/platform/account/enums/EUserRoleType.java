package com.hengxin.platform.account.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * Class Name: EUserRoleType
 * 
 * @author jishen
 * 
 */

@JsonSerialize(using = PageEnumSerializer.class)
public enum EUserRoleType implements PageEnum{

	NULL("", ""), INVESTOR("01", "投资"), FINANCER("02", "融资"),INV_FIN("03","投融资")
	,GUARANTEE("04","担保机构"),INV_GUAR("05","投担"),FIN_GUAR("06","融担"),
	INV_FIN_GUAR("07","投融担"),PLATFORM("08","平台");

	private String code;

	private String text;

	EUserRoleType(String code, String text) {
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
