package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Class Name: SubAcctBalPo
 * 
 * @author jishen
 * 会员综合账户子账户表
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AC_SUB_ACCT")
@IdClass(SubAcctPoPk.class)
public class SubAcctPo implements Serializable {

	@Id
	@Column(name = "ACCT_NO")
	private String acctNo;
	
	@Id
	@Column(name = "SUB_ACCT_NO")
	private String subAcctNo;
	
	@Column(name = "ACCT_TYPE")
	private String acctType;
	
	@Column(name = "BAL")
	private BigDecimal bal;
	
	@Column(name = "MAX_CASHABLE_AMT")
	private BigDecimal maxCashableAmt;
	
	@Column(name = "FREEZABLE_AMT")
	private BigDecimal freezableAmt;
	
	@Column(name = "RESERVED_AMT")
	private BigDecimal reservedAmt;
	
	@Column(name = "INTR_BAL")
	private BigDecimal intrBal;
	
	@Column(name = "ACCRUED_INTR_AMT")
	private BigDecimal accruedIntrAmt;
	
	@Column(name = "DEBT_AMT")
	private BigDecimal debtAmt;
	
	@Column(name = "CREATE_OPID")
	private String createOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;
	
	@Column(name = "LAST_MNT_OPID")
	private String lastMntOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MNT_TS")
	private Date lastMntTs;
	
	@Version
    @Column(name = "VERSION_CT")
	private Long versionCt;

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}

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

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}

	public BigDecimal getMaxCashableAmt() {
		return maxCashableAmt;
	}

	public void setMaxCashableAmt(BigDecimal maxCashableAmt) {
		this.maxCashableAmt = maxCashableAmt;
	}

	public BigDecimal getFreezableAmt() {
		return freezableAmt;
	}

	public void setFreezableAmt(BigDecimal freezableAmt) {
		this.freezableAmt = freezableAmt;
	}

	public BigDecimal getReservedAmt() {
		return reservedAmt;
	}

	public void setReservedAmt(BigDecimal reservedAmt) {
		this.reservedAmt = reservedAmt;
	}

	public BigDecimal getIntrBal() {
		return intrBal;
	}

	public void setIntrBal(BigDecimal intrBal) {
		this.intrBal = intrBal;
	}

	public BigDecimal getAccruedIntrAmt() {
		return accruedIntrAmt;
	}

	public void setAccruedIntrAmt(BigDecimal accruedIntrAmt) {
		this.accruedIntrAmt = accruedIntrAmt;
	}

	public BigDecimal getDebtAmt() {
		return debtAmt;
	}

	public void setDebtAmt(BigDecimal debtAmt) {
		this.debtAmt = debtAmt;
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

	public String getLastMntOpid() {
		return lastMntOpid;
	}

	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

	
}
