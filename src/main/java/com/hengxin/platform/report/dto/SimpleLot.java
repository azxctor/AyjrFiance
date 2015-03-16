package com.hengxin.platform.report.dto;

import java.io.Serializable;

public class SimpleLot implements Serializable {

	private static final long serialVersionUID = 1L;
	private String lotId;
	private String userId;

	public SimpleLot() {

	}

	public SimpleLot(String lotId, String userId) {
		this.lotId = lotId;
		this.userId = userId;
	}

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
