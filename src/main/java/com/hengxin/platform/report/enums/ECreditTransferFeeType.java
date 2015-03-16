package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum ECreditTransferFeeType implements PageEnum {
    SELLER("S", "出让人"), BUYER("B", "受让人"), SELLER_BUYER("SB", "出让人或受让人"), CODE("C", "债权代码 "), CONTRACT("con", "协议编号"), MAIN_CONTRACT(
            "MC", "主合同编号");

    private String code;

    private String text;

    ECreditTransferFeeType(String code, String text) {
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
