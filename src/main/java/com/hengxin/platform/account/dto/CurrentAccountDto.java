package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.fund.enums.EAcctStatus;
import com.hengxin.platform.risk.enums.ERiskBearLevel;

/**
 * Class Name: currentAccount
 * 
 * @author jishen
 * 
 */
public class CurrentAccountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 资金账户余额
	 */
	private BigDecimal bal;
	
	/**
	 * 可用余额
	 */
	private String availableBal;
	
	/**
	 * 可提现金额
	 */
	private String cashableAmt;
	
	/**
	 * 账户状态
	 */
	private EAcctStatus acctStatus; 
	
	/**
	 * 会员持有债权本金
	 */
	private BigDecimal totalPrincipal;
	
	/**
	 * 会员总资产
	 */
	private BigDecimal totalAssets;
    /**
     * 会员风险等级 
     */
    private ERiskBearLevel riskBearLevel;
	
//	/**
//	 * 保留金额
//	 */
//	private BigDecimal reservedAmt;
//	
//	/**
//	 * 待收回款
//	 */
//	private BigDecimal uncollecteAmt;

	public BigDecimal getTotalPrincipal() {
		return totalPrincipal;
	}

	public void setTotalPrincipal(BigDecimal totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}

	public BigDecimal getTotalAssets() {
		return totalAssets;
	}

	public void setTotalAssets(BigDecimal totalAssets) {
		this.totalAssets = totalAssets;
	}

	public String getCashableAmt() {
        return cashableAmt;
    }

    public void setCashableAmt(String cashableAmt) {
        this.cashableAmt = cashableAmt;
    }

    public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}

	public String getAvailableBal() {
		return availableBal;
	}

	public void setAvailableBal(String availableBal) {
		this.availableBal = availableBal;
	}

	public EAcctStatus getAcctStatus() {
		return acctStatus;
	}

	public void setAcctStatus(EAcctStatus acctStatus) {
		this.acctStatus = acctStatus;
	}

	public ERiskBearLevel getRiskBearLevel() {
		return riskBearLevel;
	}

	public void setRiskBearLevel(ERiskBearLevel riskBearLevel) {
		this.riskBearLevel = riskBearLevel;
	}

//	public BigDecimal getReservedAmt() {
//		return reservedAmt;
//	}
//
//	public void setReservedAmt(BigDecimal reservedAmt) {
//		this.reservedAmt = reservedAmt;
//	}
//
//	public BigDecimal getUncollecteAmt() {
//		return uncollecteAmt;
//	}
//
//	public void setUncollecteAmt(BigDecimal uncollecteAmt) {
//		this.uncollecteAmt = uncollecteAmt;
//	}
}
