package com.hengxin.platform.product.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EPayMethodType implements PageEnum {

    NULL("", "", ""), 
    ONCE_FOR_ALL("01", "到期一次还本付息", "1"), 
    MONTH_INTEREST("02", "按月等额还息，到期一次还本", "2"), 
    MONTH_PRINCIPAL_INTEREST("03", "按月等本等息", "3"), 
    MONTH_AVERAGE_INTEREST("04", "按月等额本息", "4");

    private String code;

    private String text;
    
    private String label;

    EPayMethodType(String code, String text, String label) {
        this.code = code;
        this.text = text;
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
    
}
