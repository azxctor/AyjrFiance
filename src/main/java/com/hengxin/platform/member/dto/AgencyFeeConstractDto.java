package com.hengxin.platform.member.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.hengxin.platform.member.enums.EAgencyFeeContractType;

@SuppressWarnings("serial")
public class AgencyFeeConstractDto implements Serializable {
	private Integer id;

	private String orgId;

	private EAgencyFeeContractType contractType;

	@NotEmpty(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.name.empty}")
	private String contractName;
	@NotEmpty(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.date.invalid}")
	private String startDt;
	@NotEmpty(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.date.invalid}")
	private String endDt;

	@NotNull(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.rate.invalid}")
	@Range(groups = { AgencyFeeContractSave.class }, max = 1, min = 0, message = "{member.error.contract.rate.invalid}")
	private BigDecimal month3RT;
	@NotNull(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.rate.invalid}")
	@Range(groups = { AgencyFeeContractSave.class }, max = 1, min = 0, message = "{member.error.contract.rate.invalid}")
	private BigDecimal month6RT;
	@NotNull(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.rate.invalid}")
	@Range(groups = { AgencyFeeContractSave.class }, max = 1, min = 0, message = "{member.error.contract.rate.invalid}")
	private BigDecimal month9RT;
	@NotNull(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.rate.invalid}")
	@Range(groups = { AgencyFeeContractSave.class }, max = 1, min = 0, message = "{member.error.contract.rate.invalid}")
	private BigDecimal month12RT;
	@NotNull(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.rate.invalid}")
	@Range(groups = { AgencyFeeContractSave.class }, max = 1, min = 0, message = "{member.error.contract.rate.invalid}")
	private BigDecimal allocRT;
	/**
	 * 每月核算标准比例
	 */
	@NotNull(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.rate.invalid}")
	@Range(groups = { AgencyFeeContractSave.class }, max = 1, min = 0, message = "{member.error.contract.rate.invalid}")
	private BigDecimal actgStdRT;
	/**
	 * 业务类型
	 */
	private String bisunessType;
	/**
	 * 负责人
	 */
	private String director;
	/**
	 * 渠道负责人
	 */
	private String chanDirector;

	/**
	 * 席位费标准
	 */
	@NotNull(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.numberic.invalid}")
	@Range(groups = { AgencyFeeContractSave.class }, message = "{member.error.contract.numberic.invalid}")
	private BigDecimal seatFeeAmt;

	private String note;

	private String state;

	private String acctNo;

	private String fullName;

	private long version;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public EAgencyFeeContractType getContractType() {
		return contractType;
	}

	public void setContractType(EAgencyFeeContractType contractType) {
		this.contractType = contractType;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getStartDt() {
		return startDt;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public BigDecimal getMonth3RT() {
		return month3RT;
	}

	public void setMonth3RT(BigDecimal month3rt) {
		month3RT = month3rt;
	}

	public BigDecimal getMonth6RT() {
		return month6RT;
	}

	public void setMonth6RT(BigDecimal month6rt) {
		month6RT = month6rt;
	}

	public BigDecimal getMonth9RT() {
		return month9RT;
	}

	public void setMonth9RT(BigDecimal month9rt) {
		month9RT = month9rt;
	}

	public BigDecimal getMonth12RT() {
		return month12RT;
	}

	public void setMonth12RT(BigDecimal month12rt) {
		month12RT = month12rt;
	}

	public BigDecimal getAllocRT() {
		return allocRT;
	}

	public void setAllocRT(BigDecimal allocRT) {
		this.allocRT = allocRT;
	}

	public BigDecimal getActgStdRT() {
		return actgStdRT;
	}

	public void setActgStdRT(BigDecimal actgStdRT) {
		this.actgStdRT = actgStdRT;
	}

	public String getBisunessType() {
		return bisunessType;
	}

	public void setBisunessType(String bisunessType) {
		this.bisunessType = bisunessType;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getChanDirector() {
		return chanDirector;
	}

	public void setChanDirector(String chanDirector) {
		this.chanDirector = chanDirector;
	}

	public BigDecimal getSeatFeeAmt() {
		return seatFeeAmt;
	}

	public void setSeatFeeAmt(BigDecimal seatFeeAmt) {
		this.seatFeeAmt = seatFeeAmt;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public interface AgencyFeeContractSave {
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	};

}
