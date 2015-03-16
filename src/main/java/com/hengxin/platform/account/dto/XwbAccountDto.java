package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Name: xwbAccount
 * 
 * @author jishen
 * 
 */
public class XwbAccountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 小微宝账户余额
	 */
	private BigDecimal bal;
	
	/**
	 * 年化收益率
	 */
	private String rate;
	
	/**
	 * 昨日收益
	 */
	private BigDecimal earningsYesterday;
	
	/**
	 * 累计收益
	 */
	private BigDecimal accumCrAmt;

	public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}

	public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public BigDecimal getEarningsYesterday() {
		return earningsYesterday;
	}

	public void setEarningsYesterday(BigDecimal earningsYesterday) {
		this.earningsYesterday = earningsYesterday;
	}

	public BigDecimal getAccumCrAmt() {
		return accumCrAmt;
	}

	public void setAccumCrAmt(BigDecimal accumCrAmt) {
		this.accumCrAmt = accumCrAmt;
	}
}
