package com.hengxin.platform.product.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EPackageStatus implements PageEnum {

    NULL("", "全部"),PRE_PUBLISH("PP","预发布"), WAIT_SUBSCRIBE("WS", "待申购"), SUBSCRIBE("S", "申购中"), WAIT_SIGN("WN", "待签约"), WAIT_LOAD_APPROAL("WL",
            "待放款审批"),WAIT_LOAD_APPROAL_CONFIRM("LC","待放款"), ABANDON("A", "订单废弃"), PAYING("PN","还款中"), END("E","结清"), TRANSFERING("T","还款中（转让中）"), TRANSEND("TE", "还款中（已转让）");

    private String code;

    private String text;

    EPackageStatus(String code, String text) {
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
