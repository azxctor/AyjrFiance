package com.hengxin.platform.product.dto;

import java.math.BigDecimal;

public class PkgClearingPaymentAmtDto {

	private String acctNo;

	private String pkgId;

	private Integer period;

	private BigDecimal prinAmt;

	private BigDecimal intrAmt;

	private BigDecimal loanDepositAmt;

	private BigDecimal wrtrDepositAmt;

	private BigDecimal fncrCanPaymentAmt;//融资人账户可还款总额（不包括保证金）

	private String lastFlag;
	
	private BigDecimal totalCanPaymentAmt;/*
										 * if lastFlag ? totalCanPaymentAmt =
										 * fncrCanPaymentAmt + loanDepositAmt
										 */

	public BigDecimal getTotalCanPaymentAmt() {
		return totalCanPaymentAmt;
	}

	public void setTotalCanPaymentAmt(BigDecimal totalCanPaymentAmt) {
		this.totalCanPaymentAmt = totalCanPaymentAmt;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public BigDecimal getPrinAmt() {
		return prinAmt;
	}

	public void setPrinAmt(BigDecimal prinAmt) {
		this.prinAmt = prinAmt;
	}

	public BigDecimal getIntrAmt() {
		return intrAmt;
	}

	public void setIntrAmt(BigDecimal intrAmt) {
		this.intrAmt = intrAmt;
	}

	public BigDecimal getLoanDepositAmt() {
		return loanDepositAmt;
	}

	public void setLoanDepositAmt(BigDecimal loanDepositAmt) {
		this.loanDepositAmt = loanDepositAmt;
	}

	public BigDecimal getWrtrDepositAmt() {
		return wrtrDepositAmt;
	}

	public void setWrtrDepositAmt(BigDecimal wrtrDepositAmt) {
		this.wrtrDepositAmt = wrtrDepositAmt;
	}

	public BigDecimal getFncrCanPaymentAmt() {
		return fncrCanPaymentAmt;
	}

	public void setFncrCanPaymentAmt(BigDecimal fncrCanPaymentAmt) {
		this.fncrCanPaymentAmt = fncrCanPaymentAmt;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getLastFlag() {
		return lastFlag;
	}

	public void setLastFlag(String lastFlag) {
		this.lastFlag = lastFlag;
	}

}
