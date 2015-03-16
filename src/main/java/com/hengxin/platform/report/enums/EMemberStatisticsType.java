package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EMemberStatisticsType implements PageEnum {
    NULL("", "不限"), PACKAGE("01", "按融资包"), FINANCIER("02", "按投资人");

    private String code;

    private String text;

    EMemberStatisticsType(String code, String text) {
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
