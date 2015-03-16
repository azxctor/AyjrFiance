package com.hengxin.platform.fund.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EFundDealStatus implements PageEnum {

    NULL("", ""), ALL("ALL", "全部"), DEALING("01", "处理中"), SUCCED("02", "处理成功"), FAILED("03", "处理失败");

    private String code;

    private String text;

    private EFundDealStatus(String code, String text) {
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
