package com.hengxin.platform.member.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.member.enums.EAgencyFeeContractType;
import com.hengxin.platform.member.enums.converter.EAgencyFeeContractTypeConverter;

@SuppressWarnings("serial")
@Entity
@Table(name = "V_AGENCY_FEE_CONTRACT_VIEW")
public class AuthzdCtrAgencyFeeContractInfoView implements Serializable {

	@Id
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "NAME")
	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;

	@Column(name = "ACCT_NO")
	private String acctNo;

	@Column(name = "CONTRACT_NUMS")
	private int contractNums;

	@Column(name = "START_DT")
	private Date startDt;

	@Column(name = "END_DT")
	private Date endDt;

	@Column(name = "CNTRCT_TYPE")
	@Convert(converter = EAgencyFeeContractTypeConverter.class)
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

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public Date getStartDt() {
		return startDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
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

	public EAgencyFeeContractType getContractType() {
		return contractType;
	}

	public void setContractType(EAgencyFeeContractType contractType) {
		this.contractType = contractType;
	}

}
