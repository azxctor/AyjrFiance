package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum ECreditTransferKeyType implements PageEnum {
    SELLER("S", "出让人（姓名、交易账号）"), BUYER("B", "受让人（姓名、交易账号）"), CODE("C", "融资包编号 ");

    private String code;

    private String text;

    ECreditTransferKeyType(String code, String text) {
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
