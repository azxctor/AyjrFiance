package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * AssetDto.
 * 
 */
public class AssetDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName = "";
	private String accountNum = "";
	private String totalAsset = "";
	private String availableAsset = "";
	private String cashableAmt = "";
	private String memberType;
	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(String totalAsset) {
		this.totalAsset = totalAsset;
	}

	public String getAvailableAsset() {
		return availableAsset;
	}

	public void setAvailableAsset(String availableAsset) {
		this.availableAsset = availableAsset;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCashableAmt() {
		return cashableAmt;
	}

	public void setCashableAmt(String cashableAmt) {
		this.cashableAmt = cashableAmt;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

}
