package com.hengxin.platform.member.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EInvestorLevel implements PageEnum{
	
	NULL("",""),
	COMMON("00", "普通"),
	ONE_STAR("01", "★"),
	TWO_STAR("02", "★★"),
	THREE_STAR("03", "★★★"),
	FOUR_STAR("04", "★★★★"),
	FIVE_STAR("05", "★★★★★");

    private String code;

    private String text;

    EInvestorLevel(String code, String text) {
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
