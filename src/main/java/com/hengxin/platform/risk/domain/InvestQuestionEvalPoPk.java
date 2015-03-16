package com.hengxin.platform.risk.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class InvestQuestionEvalPoPk implements Serializable {
	
    private String userId;
	
	private Date workDate;

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
	
}
