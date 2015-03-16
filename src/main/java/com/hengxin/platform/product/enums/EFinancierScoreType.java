package com.hengxin.platform.product.enums;

import java.math.BigDecimal;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EFinancierScoreType implements PageEnum {

    NULL("", ""), AZERO("2", "A(2分)"), AMIN("1.75", "A-(1.75分)"), BZERO("1.5", "B(1.5分)"), BMIN("1.25", "B-(1.25分)"), CZERO(
            "1", "C(1分)"), CMIN("0.75", "C-(0.75分)"), DZERO("0.5", "D(0.5分)");

    private String code;

    private String text;

    EFinancierScoreType(String code, String text) {
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

    public static EFinancierScoreType getEFinancierScoreType(BigDecimal sc) {
        for (EFinancierScoreType pst : EFinancierScoreType.values()) {
            String score = String.valueOf(sc);
            if (score.equals(pst.getCode())) {
                return pst;
            }
        }
        return EFinancierScoreType.NULL;
    }

}
