package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 借款合同表
 */
@Entity
@Table(name = "FP_LOAN_CNTRCT")
public class LoanContract implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CNTRCT_ID")
	private String contractId;

	@Column(name = "UNIT")
	private Integer unit;

	@Column(name = "OLD_CNTRCT_ID")
	private String oldContractId;

	@Column(name = "CREATE_OPID")
	private String createOpid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public String getOldContractId() {
		return oldContractId;
	}

	public void setOldContractId(String oldContractId) {
		this.oldContractId = oldContractId;
	}

	public String getCreateOpid() {
		return createOpid;
	}

	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

}
