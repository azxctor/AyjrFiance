package com.hengxin.platform.account.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * Class Name: ERoleType
 * 
 * @author jishen
 * 
 */

@JsonSerialize(using = PageEnumSerializer.class)
public enum ERoleType implements PageEnum {

	NULL("", ""), INVESTOR("01", "投"), FINANCER("02", "融"),INV_FIN("03","投/融")
	,GUARANTEE("04","担"),INV_GUAR("05","投/担"),FIN_GUAR("06","融/担"),
	INV_FIN_GUAR("07","投/融/担"),PLATFORM("08","");

	private String code;

	private String text;

	ERoleType(String code, String text) {
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
