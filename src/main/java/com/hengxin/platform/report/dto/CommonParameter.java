package com.hengxin.platform.report.dto;

import java.io.Serializable;

public class CommonParameter implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exportFileExt;
    private String parentClientEvent;
    private String eventName;
    private String keyWord; // 关键字
    private String commonId; // Id
    private String productId;

    public String getExportFileExt() {
        return exportFileExt;
    }

    public void setExportFileExt(String exportFileExt) {
        this.exportFileExt = exportFileExt;
    }

    public String getParentClientEvent() {
        return parentClientEvent;
    }

    public void setParentClientEvent(String parentClientEvent) {
        this.parentClientEvent = parentClientEvent;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getCommonId() {
        return commonId;
    }

    public void setCommonId(String commonId) {
        this.commonId = commonId;
    }

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
