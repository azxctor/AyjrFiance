package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/*
 * 
 */
/**
 * CreditorMarketdownDto.
 *
 */
public class CreditorMarketdownDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId;

	private String productName;

	private String state;

	private String riskLevel;

	private String riskSafeguard;

	private String annualProfit;

	private String limitTime;

	private String startTime;

	private String financedAmount;

	private Boolean aipFlag;

	private String accumulateAmount;

	private String availableAsset;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getRiskSafeguard() {
		return riskSafeguard;
	}

	public void setRiskSafeguard(String riskSafeguard) {
		this.riskSafeguard = riskSafeguard;
	}

	public String getAnnualProfit() {
		return annualProfit;
	}

	public void setAnnualProfit(String annualProfit) {
		this.annualProfit = annualProfit;
	}

	public String getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(String limitTime) {
		this.limitTime = limitTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFinancedAmount() {
		return financedAmount;
	}

	public void setFinancedAmount(String financedAmount) {
		this.financedAmount = financedAmount;
	}

	public Boolean getAipFlag() {
		return aipFlag;
	}

	public void setAipFlag(Boolean aipFlag) {
		this.aipFlag = aipFlag;
	}

	public String getAccumulateAmount() {
		return accumulateAmount;
	}

	public void setAccumulateAmount(String accumulateAmount) {
		this.accumulateAmount = accumulateAmount;
	}

	public String getAvailableAsset() {
		return availableAsset;
	}

	public void setAvailableAsset(String availableAsset) {
		this.availableAsset = availableAsset;
	}

}