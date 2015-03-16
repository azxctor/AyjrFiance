package com.hengxin.platform.account.dto.biz.req;

import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * 转账请求
 * @author jishen
 *
 */
public class TransferAccountReq {
	
	/**
	 * 事件编号
	 */
	private String eventId;

	/**
	 * 转账金额
	 */
	private BigDecimal trxAmt;
	
	/**
	 * 转账备注 
	 */
	private String trxMemo;
	
	/**
	 * 付款方会员id
	 */
	private String payerUserId;
	
	/**
	 * 付款方账户号
	 */
	private String payerAcctNo;
	
	/**
	 * 收款方会员Id
	 */
	private String payeeUserId;
	
	/**
	 * 收款方账户号
	 */
	private String payeeAcctNo;
	
	/**
	 * 关联编号
	 */
	private String bizId;
	
	/**
	 * 当前操作用户Id
	 */
	private String currOpId;
	
	/**
	 * 工作日期
	 */
	private Date workDate;
	
	/**
	 * 资金用途
	 */
	private EFundUseType useType;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public String getPayerUserId() {
		return payerUserId;
	}

	public void setPayerUserId(String payerUserId) {
		this.payerUserId = payerUserId;
	}

	public String getPayeeUserId() {
		return payeeUserId;
	}

	public void setPayeeUserId(String payeeUserId) {
		this.payeeUserId = payeeUserId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getCurrOpId() {
		return currOpId;
	}

	public void setCurrOpId(String currOpId) {
		this.currOpId = currOpId;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public String getPayerAcctNo() {
		return payerAcctNo;
	}

	public void setPayerAcctNo(String payerAcctNo) {
		this.payerAcctNo = payerAcctNo;
	}

	public String getPayeeAcctNo() {
		return payeeAcctNo;
	}

	public void setPayeeAcctNo(String payeeAcctNo) {
		this.payeeAcctNo = payeeAcctNo;
	}

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}
}
