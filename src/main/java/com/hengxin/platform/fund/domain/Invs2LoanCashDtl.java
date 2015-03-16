package com.hengxin.platform.fund.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投转贷提现明细
 * @author dcliu
 *
 */
public class Invs2LoanCashDtl {

	private String jnlNo;
	
	private String acctNo;
	
	private String repayFlg;
	
	private Date trxDt;
	
	private Date repayDt;
	
	private BigDecimal realCashAmt;
	
	private BigDecimal maxCashAmt;
	
	private BigDecimal ownAmt;
	
	private BigDecimal loanAmt;
	
	private String createOpId;
	
	private Date createTs;
	
	private String lastMntOpid;
	
	private Date lastMntTs;

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
