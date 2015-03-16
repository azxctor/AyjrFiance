package com.hengxin.platform.product.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum ETransferStatus implements PageEnum {
    
    NULL("", ""), ACTIVE("01", "转让中"), SUCCESS("02","转让成功"), CANCELLED("03", 
            "已撤单"), OVERDUE("04", "已过期");

    private String code;

    private String text;

    ETransferStatus(String code, String text) {
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
