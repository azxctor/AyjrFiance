package com.hengxin.platform.member.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.BaseMaintainablePo;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.member.enums.EAgencyFeeContractType;
import com.hengxin.platform.member.enums.converter.EAgencyFeeContractTypeConverter;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_INFO_FEE_CNTRCT")
@SequenceGenerator(name = "cntrct_seq", sequenceName = "UM_INFO_FEE_CNTRCT_ID_SEQ", allocationSize = 1)
public class AgencyFeeConstractPo extends BaseMaintainablePo implements Serializable {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cntrct_seq")
	private Integer id;

	@Column(name = "ORG_ID")
	private String orgId;

	@Column(name = "CNTRCT_TYPE")
	@Convert(converter = EAgencyFeeContractTypeConverter.class)
	private EAgencyFeeContractType contractType;

	@Column(name = "CNTRCT_NAME")
	private String contractName;

	@Column(name = "START_DT")
	private Date startDt;

	@Column(name = "END_DT")
	private Date endDt;

	@Column(name = "MONTH_3_RT")
	private BigDecimal month3RT;

	@Column(name = "MONTH_6_RT")
	private BigDecimal month6RT;

	@Column(name = "MONTH_9_RT")
	private BigDecimal month9RT;

	@Column(name = "MONTH_12_RT")
	private BigDecimal month12RT;

	@Column(name = "ALLOC_RT")
	private BigDecimal allocRT;

	@Column(name = "ACTG_STD_RT")
	private BigDecimal actgStdRT;
	/**
	 * 业务类型
	 */
	@Column(name = "BIZ_TYPE")
	private String bisunessType;
	/**
	 * 负责人
	 */
	@Column(name = "DIRECTOR")
	private String director;
	/**
	 * 渠道负责人
	 */
	@Column(name = "CHAN_DIRECTOR")
	private String chanDirector;

	/**
	 * 席位费标准
	 */
	@Column(name = "SEAT_FEE_AMT")
	private BigDecimal seatFeeAmt;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "STATE")
	private String state;

	@Version
	@Column(name = "VERSION_CT")
	private long version;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORG_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
	private ServiceCenterInfo agency;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORG_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
	private AcctPo acctPo;

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

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public ServiceCenterInfo getAgency() {
		return agency;
	}

	public void setAgency(ServiceCenterInfo agency) {
		this.agency = agency;
	}

	public AcctPo getAcctPo() {
		return acctPo;
	}

	public void setAcctPo(AcctPo acctPo) {
		this.acctPo = acctPo;
	}

}