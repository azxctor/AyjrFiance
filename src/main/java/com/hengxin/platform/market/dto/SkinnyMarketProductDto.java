package com.hengxin.platform.market.dto;

import java.io.Serializable;

public class SkinnyMarketProductDto implements Serializable {

	private static final long serialVersionUID = 5L;

	private String rate; // 年利率
	
	private String termLength; // 期限长度

	/**
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}

	/**
	 * @return the termLength
	 */
	public String getTermLength() {
		return termLength;
	}

	/**
	 * @param termLength the termLength to set
	 */
	public void setTermLength(String termLength) {
		this.termLength = termLength;
	}
	
}
