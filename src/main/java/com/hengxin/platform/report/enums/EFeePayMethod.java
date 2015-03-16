package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EFeePayMethod implements PageEnum{
    
    NULL("", "全部"), ONCE("01", "一次性收费"), MONTH("02", "按期收费");
    
    private String code;

    private String text;

    EFeePayMethod(String code, String text) {
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
