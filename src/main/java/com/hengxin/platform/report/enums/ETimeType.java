package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum ETimeType implements PageEnum {
    NULL("", "不限"), SIGN("01", "签约日期"), SUBSCRIBE("02", "申购日期");

    private String code;

    private String text;

    ETimeType(String code, String text) {
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
