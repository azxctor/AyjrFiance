package com.hengxin.platform.report.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * @deprecated<br/>
 * It should load data from UM_INVSTR_LVL by SystemDictUtil.
 *
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum EMemeberLevel implements PageEnum {
    NUll("", "全部"), COMMON("01", "普通"), VIP("02", "高级VIP"), SUPERVIP("03", "VIP"), VIP_1("04",
            "VIP-1"), SPECIALVIP("05", "特殊VIP"), XINYIDAIVIP("06", "兴易贷VIP");

    private String code;

    private String text;

    EMemeberLevel(String code, String text) {
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
