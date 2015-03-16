package com.hengxin.platform.fund.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FeePayStatePoPk implements Serializable {

	private String userId;
	
	private String feeType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
	
	
}
