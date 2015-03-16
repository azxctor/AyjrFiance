package com.hengxin.platform.common.enums;

public enum ENoticeType  implements PageEnum {

    NULL("","发给所有人"), ROLE("R","根据角色发通知");

    private String code;

    private String text;

    ENoticeType(String code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override 
    public String getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
} 
