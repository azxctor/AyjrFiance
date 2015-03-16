package com.hengxin.platform.market.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SkinnyDrawDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String packageId;
	
	private String packageName;
	
	private BigDecimal totalAmount;
	
	private BigDecimal availableAmount;
	
	private BigDecimal dealAmount;
	
	private BigDecimal summarizedAmount;
	
	private String totalAmountDesc;
	
	private String availableAmountDesc;
	
	private String dealAmountDesc;
	
	private String summarizedAmountDesc;
	
	private int candidatesNumber;

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getDealAmount() {
		return dealAmount;
	}

	public void setDealAmount(BigDecimal dealAmount) {
		this.dealAmount = dealAmount;
	}

	public BigDecimal getSummarizedAmount() {
		return summarizedAmount;
	}

	public void setSummarizedAmount(BigDecimal summarizedAmount) {
		this.summarizedAmount = summarizedAmount;
	}

	public int getCandidatesNumber() {
		return candidatesNumber;
	}

	public void setCandidatesNumber(int candidatesNumber) {
		this.candidatesNumber = candidatesNumber;
	}

	/**
	 * @return the totalAmountDesc
	 */
	public String getTotalAmountDesc() {
		return totalAmountDesc;
	}

	/**
	 * @param totalAmountDesc the totalAmountDesc to set
	 */
	public void setTotalAmountDesc(String totalAmountDesc) {
		this.totalAmountDesc = totalAmountDesc;
	}

	/**
	 * @return the availableAmountDesc
	 */
	public String getAvailableAmountDesc() {
		return availableAmountDesc;
	}

	/**
	 * @param availableAmountDesc the availableAmountDesc to set
	 */
	public void setAvailableAmountDesc(String availableAmountDesc) {
		this.availableAmountDesc = availableAmountDesc;
	}

	/**
	 * @return the dealAmountDesc
	 */
	public String getDealAmountDesc() {
		return dealAmountDesc;
	}

	/**
	 * @param dealAmountDesc the dealAmountDesc to set
	 */
	public void setDealAmountDesc(String dealAmountDesc) {
		this.dealAmountDesc = dealAmountDesc;
	}

	/**
	 * @return the summarizedAmountDesc
	 */
	public String getSummarizedAmountDesc() {
		return summarizedAmountDesc;
	}

	/**
	 * @param summarizedAmountDesc the summarizedAmountDesc to set
	 */
	public void setSummarizedAmountDesc(String summarizedAmountDesc) {
		this.summarizedAmountDesc = summarizedAmountDesc;
	}
	
}
