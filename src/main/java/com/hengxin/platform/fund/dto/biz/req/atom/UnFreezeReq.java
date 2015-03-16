package com.hengxin.platform.fund.dto.biz.req.atom;

import java.util.Date;

public class UnFreezeReq {
	
	private String userId;
	
	private String freezeJnlNo;
	
	private String trxMemo;
	
	private String currOpId;
	
	private Date workDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFreezeJnlNo() {
		return freezeJnlNo;
	}

	public void setFreezeJnlNo(String freezeJnlNo) {
		this.freezeJnlNo = freezeJnlNo;
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
