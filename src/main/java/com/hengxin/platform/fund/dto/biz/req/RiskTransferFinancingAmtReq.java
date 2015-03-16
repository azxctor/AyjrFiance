package com.hengxin.platform.fund.dto.biz.req;

import java.util.Date;

public class RiskTransferFinancingAmtReq {

	/**
	 * 事件编号
	 */
	private String eventId;
	
	/**
	 * 冻结日志流水号
	 */
	private String frzJnlNo;
	
	/**
	 * 融资包编号
	 */
	private String bizId;
	
	/**
	 * 会员编号
	 */
	private String userId;
	
	/**
	 * 备注
	 */
	private String trxMemo;
	
	/**
	 * 当前操作用户编号
	 */
	private String currOpId;
	
	/**
	 * 工作日期
	 */
	private Date workDate;

	public String getFrzJnlNo() {
		return frzJnlNo;
	}

	public void setFrzJnlNo(String frzJnlNo) {
		this.frzJnlNo = frzJnlNo;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
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

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}	
	
}
