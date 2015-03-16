package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum ECreditTransferTimeType implements PageEnum {
    SELL("S", "出让日期"), BUY("B", "受让日期");

    private String code;

    private String text;

    ECreditTransferTimeType(String code, String text) {
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
