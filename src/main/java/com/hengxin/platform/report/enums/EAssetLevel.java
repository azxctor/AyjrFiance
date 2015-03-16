package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EAssetLevel implements PageEnum {
    NUll("", "全部"), ONE("01", "1万以下"), TEN("02", "1~10万"), FIFTY("03", "10~50万"),HUNDRED("04", "50~100万"), ABOVE("05", "100万以上");

    private String code;

    private String text;

    EAssetLevel(String code, String text) {
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
