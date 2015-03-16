package com.hengxin.platform.fund.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.fund.enums.ECashPool;

/**
 * Class Name: FundPoolDtlDto Description: TODO
 * 
 * @author jishen
 * 
 */
public class FundPoolDtlDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 资金池
	 */
	private ECashPool cashPool;
	
	/**
	 * 资金池存量金额
	 */
	private BigDecimal totalBal;
	
	/**
	 * 汇总金额
	 */
	private BigDecimal totalAmt;
	
	/**
	 * 付款金额
	 */
	private BigDecimal payAmt;
	
	/**
	 * 收款金额
	 */
	private BigDecimal recvAmt;
	
	/**
	 * 收付款差值
	 */
	private BigDecimal cashRecvAmt;

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}

	public BigDecimal getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	public BigDecimal getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(BigDecimal payAmt) {
		this.payAmt = payAmt;
	}

	public BigDecimal getRecvAmt() {
		return recvAmt;
	}

	public void setRecvAmt(BigDecimal recvAmt) {
		this.recvAmt = recvAmt;
	}

	public BigDecimal getCashRecvAmt() {
		return cashRecvAmt;
	}

	public void setCashRecvAmt(BigDecimal cashRecvAmt) {
		this.cashRecvAmt = cashRecvAmt;
	}

	public BigDecimal getTotalBal() {
		return totalBal;
	}

	public void setTotalBal(BigDecimal totalBal) {
		this.totalBal = totalBal;
	}
}	