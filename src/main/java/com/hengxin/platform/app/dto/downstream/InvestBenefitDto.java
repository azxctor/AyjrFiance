package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Name: InvestBenifitDto Description: TODO
 * 
 * @author Tom
 * 
 */

public class InvestBenefitDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 周数
	 */
	private String week;
	/**
	 * 投资收益
	 */
	private BigDecimal investBenefit;

	/**
	 * InvestBenifitDto Constructor
	 * 
	 */

	public InvestBenefitDto() {
		super();
	}

	/**
	 * InvestBenifitDto Constructor
	 * 
	 * @param week
	 * @param investBenefit
	 */

	public InvestBenefitDto(String week, BigDecimal investBenefit) {
		super();
		this.week = week;
		this.investBenefit = investBenefit;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public BigDecimal getInvestBenefit() {
		return investBenefit;
	}

	public void setInvestBenefit(BigDecimal investBenefit) {
		this.investBenefit = investBenefit;
	}

}
