package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * MyInvestListdownDto.
 * 
 */
public class MyInvestListdownDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId;

	private String productName;

	private String annualProfit;

	private String limitTime;

	private String purchaseTime;
	
	private String purchaseAmount;

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

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public String getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(String purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	
}
