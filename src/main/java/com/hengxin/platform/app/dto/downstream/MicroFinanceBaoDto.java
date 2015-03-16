package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * MicroFinanceBaoDto.
 *
 */
public class MicroFinanceBaoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String totalAsset = "";
	private String yesterdayProfit = "";
	private String accumulateProfit = "";
	private String annualProfitRate = "";

	public String getAnnualProfitRate() {
		return annualProfitRate;
	}

	public void setAnnualProfitRate(String annualProfitRate) {
		this.annualProfitRate = annualProfitRate;
	}

	public String getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(String totalAsset) {
		this.totalAsset = totalAsset;
	}

	public String getYesterdayProfit() {
		return yesterdayProfit;
	}

	public void setYesterdayProfit(String yesterdayProfit) {
		this.yesterdayProfit = yesterdayProfit;
	}

	public String getAccumulateProfit() {
		return accumulateProfit;
	}

	public void setAccumulateProfit(String accumulateProfit) {
		this.accumulateProfit = accumulateProfit;
	}

}
