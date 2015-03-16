package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class WithdrawalApplSumAmtDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 汇总金额
	 */
	private BigDecimal sumAmt;

	public BigDecimal getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}
}
