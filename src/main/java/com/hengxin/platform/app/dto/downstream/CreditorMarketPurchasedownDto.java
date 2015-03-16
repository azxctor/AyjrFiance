package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * CreditorMarketPurchasedownDto
 *
 */
public class CreditorMarketPurchasedownDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String minSubscribeAmount = "";
	
	String maxSubscribeAmount = "";
	
	String totalSubscribeAmount = "";
	
	double rate;

	public String getMinSubscribeAmount() {
		return minSubscribeAmount;
	}

	public void setMinSubscribeAmount(String minSubscribeAmount) {
		this.minSubscribeAmount = minSubscribeAmount;
	}

	public String getMaxSubscribeAmount() {
		return maxSubscribeAmount;
	}

	public void setMaxSubscribeAmount(String maxSubscribeAmount) {
		this.maxSubscribeAmount = maxSubscribeAmount;
	}

	public String getTotalSubscribeAmount() {
		return totalSubscribeAmount;
	}

	public void setTotalSubscribeAmount(String totalSubscribeAmount) {
		this.totalSubscribeAmount = totalSubscribeAmount;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
}
