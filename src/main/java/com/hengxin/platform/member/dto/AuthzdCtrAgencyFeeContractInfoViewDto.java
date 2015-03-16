package com.hengxin.platform.member.dto;

import java.io.Serializable;

import com.hengxin.platform.member.enums.EAgencyFeeContractType;

@SuppressWarnings("serial")
public class AuthzdCtrAgencyFeeContractInfoViewDto implements Serializable {

	private String userId;

	private String fullName;

	private String name;

	private String createTs;

	private String acctNo;

	private int contractNums;

	private String startDt;

	private String endDt;

	private EAgencyFeeContractType contractType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateTs() {
		return createTs;
	}

	public void setCreateTs(String createTs) {
		this.createTs = createTs;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public int getContractNums() {
		return contractNums;
	}

	public void setContractNums(int contractNums) {
		this.contractNums = contractNums;
	}

	public String getStartDt() {
		return startDt;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public EAgencyFeeContractType getContractType() {
		return contractType;
	}

	public void setContractType(EAgencyFeeContractType contractType) {
		this.contractType = contractType;
	}

}