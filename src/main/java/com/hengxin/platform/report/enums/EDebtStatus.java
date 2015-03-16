package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EDebtStatus implements PageEnum {
    NUll("", "全部"), NO("N", "未结清"), YES("Y", "已结清");

    private String code;

    private String text;

    EDebtStatus(String code, String text) {
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