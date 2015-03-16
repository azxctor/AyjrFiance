package com.hengxin.platform.common.dto;

import java.io.Serializable;

public class DashboardItemDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
    private String subTitle;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
