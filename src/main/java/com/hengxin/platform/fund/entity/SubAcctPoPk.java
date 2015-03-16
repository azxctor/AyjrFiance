package com.hengxin.platform.fund.entity;

import java.io.Serializable;


@SuppressWarnings("serial")
public class SubAcctPoPk implements Serializable{
	
	private String acctNo;
	
	private String subAcctNo;

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getSubAcctNo() {
		return subAcctNo;
	}

	public void setSubAcctNo(String subAcctNo) {
		this.subAcctNo = subAcctNo;
	}
	
	
}
