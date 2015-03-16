package com.hengxin.platform.fund.dto.biz.req;

import java.math.BigDecimal;
import java.util.Date;

public class LoadnHonourAgtDepositPayReq {
	
	/**
	 *  融资会员编号
	 */
	private String userId;    
	
	/**
	 * 业务交易编号(融资包编号)
	 */
	private String bizId;
	
	/**
	 * 交易金额(支付的保证金金额)
	 */
	private BigDecimal trxAmt;
	
	/**
	 * 交易备注
	 */
	private String txMemo; 
	
	/**
	 * 当前操作员编号
	 */
	private String currOpId;
	
	/**
	 * 系统当前交易工作日期
	 */
	private Date workDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public String getTxMemo() {
		return txMemo;
	}

	public void setTxMemo(String txMemo) {
		this.txMemo = txMemo;
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
	
	
	
}
