package com.hengxin.platform.product.enums;

import java.math.BigDecimal;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EProductScoreType implements PageEnum {

    NULL("", ""), AZERO("4", "A(4分)"), AMIN("3.5", "A-(3.5分)"), BZERO("3", "B(3分)"), BMIN("2.5", "B-(2.5分)"), CZERO(
            "2", "C(2分)"), CMIN("1.5", "C-(1.5分)"), DZERO("1", "D(1分)");

    private String code;

    private String text;

    EProductScoreType(String code, String text) {
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

    public static EProductScoreType getEProductScoreType(BigDecimal sc) {
        for (EProductScoreType pst : EProductScoreType.values()) {
            String score = String.valueOf(sc);
            if (score.equals(pst.getCode())) {
                return pst;
            }
        }
        return EProductScoreType.NULL;
    }
}
