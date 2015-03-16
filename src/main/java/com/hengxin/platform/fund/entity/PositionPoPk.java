package com.hengxin.platform.fund.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PositionPoPk implements Serializable{

	private String positionId;
	
	private String userId;
	
	private String pkgId;

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}
	
}
