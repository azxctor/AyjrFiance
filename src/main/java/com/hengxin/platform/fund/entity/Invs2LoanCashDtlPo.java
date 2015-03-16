package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Class Name: Invs2LoanCashDtlPo
 * 投转贷提现明细表
 * @author dcliu
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name="AC_INVS2LOAN_CASH_DTL")
public class Invs2LoanCashDtlPo implements Serializable {

	@Id
	@Column(name = "JNL_NO")
	private String jnlNo;
	
	@Column(name = "ACCT_NO")
	private String acctNo;
	
	@Column(name = "REPAY_FLG")
	private String repayFlg;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "TRX_DT")
	private Date trxDt;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "REPAY_DT")
	private Date repayDt;
	
	@Column(name = "REAL_CASH_AMT")
	private BigDecimal realCashAmt;
	
	@Column(name = "MAX_CASH_AMT")
	private BigDecimal maxCashAmt;
	
	@Column(name = "OWN_AMT")
	private BigDecimal ownAmt;
	
	@Column(name = "LOAN_AMT")
	private BigDecimal loanAmt;
	
	@Column(name = "CREATE_OPID")
	private String createOpId;
	
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

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getRepayFlg() {
		return repayFlg;
	}

	public void setRepayFlg(String repayFlg) {
		this.repayFlg = repayFlg;
	}

	public BigDecimal getRealCashAmt() {
		return realCashAmt;
	}

	public void setRealCashAmt(BigDecimal realCashAmt) {
		this.realCashAmt = realCashAmt;
	}

	public BigDecimal getMaxCashAmt() {
		return maxCashAmt;
	}

	public void setMaxCashAmt(BigDecimal maxCashAmt) {
		this.maxCashAmt = maxCashAmt;
	}

	public BigDecimal getOwnAmt() {
		return ownAmt;
	}

	public void setOwnAmt(BigDecimal ownAmt) {
		this.ownAmt = ownAmt;
	}

	public BigDecimal getLoanAmt() {
		return loanAmt;
	}

	public void setLoanAmt(BigDecimal loanAmt) {
		this.loanAmt = loanAmt;
	}

	public String getCreateOpId() {
		return createOpId;
	}

	public void setCreateOpId(String createOpId) {
		this.createOpId = createOpId;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}

	public String getLastMntOpid() {
		return lastMntOpid;
	}

	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public Date getRepayDt() {
		return repayDt;
	}

	public void setRepayDt(Date repayDt) {
		this.repayDt = repayDt;
	}
	
}
