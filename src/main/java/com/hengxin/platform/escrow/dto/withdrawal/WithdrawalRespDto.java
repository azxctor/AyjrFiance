package com.hengxin.platform.escrow.dto.withdrawal;

import java.math.BigDecimal;

import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

public class WithdrawalRespDto {
	
	private String orderId;
	
	private String retCode;
	
	private String retMsg;
	
	private BigDecimal balance;
	
	private EOrderStatusEnum status;
	
	private String cashId;
	
	private String outOrderNo;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public EOrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
	}

	public String getCashId() {
		return cashId;
	}

	public void setCashId(String cashId) {
		this.cashId = cashId;
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}
	
}
