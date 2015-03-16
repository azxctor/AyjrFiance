package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EMemberType implements PageEnum {
    NULL("", "全部"), INVESTOR("invester", "投资"), FINANCER("financier", "融资"), INVESTOR_FINANCER("invester/financier",
            "投融资"), PRODUCTSERVICE("prod_serv", "担保机构"), AUTHZDCENTER("authenticated_service_center", "服务中心"), PRODSERV_AUTHZD(
            "prod_serv/authenticated_service_center", "服务中心/担保机构"), ;

    private String code;

    private String text;

    EMemberType(String code, String text) {
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
