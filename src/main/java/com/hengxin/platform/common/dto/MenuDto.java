package com.hengxin.platform.common.dto;

import java.io.Serializable;

public class MenuDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
    private String name;
    private String parentUrl;
    private String code;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
