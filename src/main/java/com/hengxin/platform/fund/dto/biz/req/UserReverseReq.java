package com.hengxin.platform.fund.dto.biz.req;

import java.util.Date;

public class UserReverseReq {

	private String rvsBnkJnlNo;

	private String trxMemo;

	private String currOpid;

	private Date workDate;
	
	private String dealMemo;

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

	public String getRvsBnkJnlNo() {
		return rvsBnkJnlNo;
	}

	public void setRvsBnkJnlNo(String rvsBnkJnlNo) {
		this.rvsBnkJnlNo = rvsBnkJnlNo;
	}

	public String getDealMemo() {
		return dealMemo;
	}

	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}
	

}
