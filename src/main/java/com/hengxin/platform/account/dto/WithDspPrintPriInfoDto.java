package com.hengxin.platform.account.dto;

import java.io.Serializable;

/**  
 * Class Name: WithDspPrintPriInfoDto Description: TODO
 
 */

public class WithDspPrintPriInfoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fromDate;
	private String toDate;
	private String cashPool;
	private String status;
	private String applFlag;
	private String dealFlag;
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getCashPool() {
		return cashPool;
	}
	public void setCashPool(String cashPool) {
		this.cashPool = cashPool;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getApplFlag() {
		return applFlag;
	}
	public void setApplFlag(String applFlag) {
		this.applFlag = applFlag;
	}
	public String getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}
	 
	
}
