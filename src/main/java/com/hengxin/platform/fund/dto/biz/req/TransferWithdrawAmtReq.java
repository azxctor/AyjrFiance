package com.hengxin.platform.fund.dto.biz.req;

import java.util.Date;

public class TransferWithdrawAmtReq {

	private String userId;
	
	private String jnlNo;
	
	private String trxMemo;
	
	private String currOpid;
	
	private Date workDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public String getCurrOpid() {
		return currOpid;
	}

	public void setCurrOpid(String currOpid) {
		this.currOpid = currOpid;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
	
}
