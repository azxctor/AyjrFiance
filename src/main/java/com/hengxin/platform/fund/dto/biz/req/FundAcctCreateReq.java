package com.hengxin.platform.fund.dto.biz.req;

import java.util.Date;

import com.hengxin.platform.security.enums.EBizRole;

public class FundAcctCreateReq {

	private String userId;
	
	private Date workDate;
	
	private String currOpId;
	
	private EBizRole assignRole;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public String getCurrOpId() {
		return currOpId;
	}

	public void setCurrOpId(String currOpId) {
		this.currOpId = currOpId;
	}

	public EBizRole getAssignRole() {
		return assignRole;
	}

	public void setAssignRole(EBizRole assignRole) {
		this.assignRole = assignRole;
	}
	
}
