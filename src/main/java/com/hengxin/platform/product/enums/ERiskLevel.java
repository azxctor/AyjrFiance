package com.hengxin.platform.product.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum ERiskLevel implements PageEnum{
	
	NULL("Z", "", "", ""), HIGH_QUALITY("A", "A", "A", "1"), MEDIUM("B", "B", "B", "2"), ACCEPTABLE("C", "C", "C", "3"), HIGH_RISK("D", "D", "D", "4");

    private String code;

    private String text;

    private String textShort;
    
    private String label;

    ERiskLevel(String code, String text, String textShort, String label) {
        this.code = code;
        this.text = text;
        this.textShort = textShort;
        this.label = label;
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

	public String getLabel() {
		return label;
	}

	public String getTextShort() {
		return textShort;
	}
}

