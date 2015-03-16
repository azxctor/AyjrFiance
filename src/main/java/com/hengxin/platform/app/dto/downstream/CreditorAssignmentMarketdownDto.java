package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * CreditorAssignmentMarketdownDto
 *
 */
public class CreditorAssignmentMarketdownDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId;
	
	private String productName;
	
	private String riskLevel;
	
	private String riskSafeguard;
	
	private String annualProfit;
	
	private String quotedPrice;
	
	private String remainingPrincipalAndInterest;
	
	private String deadline;
	
	private String repaymentManner;

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

	public String getQuotedPrice() {
		return quotedPrice;
	}

	public void setQuotedPrice(String quotedPrice) {
		this.quotedPrice = quotedPrice;
	}

	public String getRemainingPrincipalAndInterest() {
		return remainingPrincipalAndInterest;
	}

	public void setRemainingPrincipalAndInterest(
			String remainingPrincipalAndInterest) {
		this.remainingPrincipalAndInterest = remainingPrincipalAndInterest;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getRepaymentManner() {
		return repaymentManner;
	}

	public void setRepaymentManner(String repaymentManner) {
		this.repaymentManner = repaymentManner;
	}
	
}
