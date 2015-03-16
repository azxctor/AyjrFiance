package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Name: currentAccount
 * 
 * @author jishen
 * 
 */
public class EarningsInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 预计收益
	 */
	private BigDecimal prospectiveEarnings;
	
	/**
	 * 待收收益
	 */
	private BigDecimal uncollecteEarnings;
	
	/**
	 * 已收收益
	 */
	private BigDecimal collecteEarnings;
	
	/**
	 * 累计投资总额
	 */
	private BigDecimal totalInvestment;

	public BigDecimal getProspectiveEarnings() {
		return prospectiveEarnings;
	}

	public void setProspectiveEarnings(BigDecimal prospectiveEarnings) {
		this.prospectiveEarnings = prospectiveEarnings;
	}

	public BigDecimal getUncollecteEarnings() {
		return uncollecteEarnings;
	}

	public void setUncollecteEarnings(BigDecimal uncollecteEarnings) {
		this.uncollecteEarnings = uncollecteEarnings;
	}

	public BigDecimal getCollecteEarnings() {
		return collecteEarnings;
	}

	public void setCollecteEarnings(BigDecimal collecteEarnings) {
		this.collecteEarnings = collecteEarnings;
	}

	public BigDecimal getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(BigDecimal totalInvestment) {
		this.totalInvestment = totalInvestment;
	}
}

