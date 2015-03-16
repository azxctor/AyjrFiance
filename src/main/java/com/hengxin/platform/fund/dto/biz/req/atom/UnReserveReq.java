package com.hengxin.platform.fund.dto.biz.req.atom;

import java.util.Date;

public class UnReserveReq {

	/**
	 * 会员编号
	 */
	private String userId;
	
	/**
	 * 保留日志流水号
	 */
	private String reserveJnlNo;
	
	/**
	 * 备注
	 */
	private String trxMemo;
	
	/**
	 * 当前操作用户
	 */
	private String currOpId;
	
	/**
	 * 当前工作日期
	 */
	private Date workDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReserveJnlNo() {
		return reserveJnlNo;
	}

	public void setReserveJnlNo(String reserveJnlNo) {
		this.reserveJnlNo = reserveJnlNo;
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
	
}
