package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EYesOrNo implements PageEnum {
    NUll("", "全部"), YES("Y", "是"), NO("N", "否");

    private String code;

    private String text;

    EYesOrNo(String code, String text) {
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
