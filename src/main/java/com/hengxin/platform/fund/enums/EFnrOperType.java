package com.hengxin.platform.fund.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * 冻结保留操作类型
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum EFnrOperType implements PageEnum {

	FREEZE("F","冻结"),
	FREEZE_BIZ_NOPAY("FB","业务类只收不付冻结"),
	FREEZE_MGT_NOPAY("FM","管理类只收不付冻结"),
	RESERVE("R","保留"),
	RESERVE_ASSET("RA","资产保留");
	
	private String code;

    private String text;
	
	private EFnrOperType(String code, String text){
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
