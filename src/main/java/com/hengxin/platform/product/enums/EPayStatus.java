package com.hengxin.platform.product.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EPayStatus implements PageEnum {

    NULL("", ""), NORMAL("01", "还款中"), OVERDUE("02", "违约中"), COMPENSATING("03", "代偿中"), COMPENSATORY("04", "已代偿"), CLEARED(
            "05", "已清分"), BADDEBT("BD","待追偿"), FINISH("99", "结束");

    private String code;

    private String text;

    EPayStatus(String code, String text) {
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
