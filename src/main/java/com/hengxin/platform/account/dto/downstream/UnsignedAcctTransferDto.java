package com.hengxin.platform.account.dto.downstream;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class UnsignedAcctTransferDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 充值金额
     */
    @NotNull
	private BigDecimal amount;
	
	/**
     * 交易账户号
     */
    @NotNull
	private String acctNo;
	
	/**
	 * 摘要
	 */
	private String memo;
	
	/**
	 * 回单号
	 */
    @NotNull
	private String bkSerial;
	
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getBkSerial() {
		return bkSerial;
	}
	public void setBkSerial(String bkSerial) {
		this.bkSerial = bkSerial;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
