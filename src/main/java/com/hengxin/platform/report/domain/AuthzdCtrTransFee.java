package com.hengxin.platform.report.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "V_AUTHZD_CTR_TRANSFER_FEE")
public class AuthzdCtrTransFee implements Serializable {
	
	@Id
	@Column(name = "JNL_NO")
	private String jnlNo;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "ACCT_NO")
	private String acctNo;

	@Column(name = "PKG_ID")
	private String pkgId;

	@Column(name = "TRX_TYPE")
	private String trxType;

	@Temporal(TemporalType.DATE)
	@Column(name = "TRX_DT")
	private Date trxDt;

	@Column(name = "TRX_AMT")
	private BigDecimal trxAmt;

	@Column(name = "AGENT_NAME")
	private String agentName;

	@Column(name = "AUTHZD_CTR_ID")
	private String authzdCtrId;
	
	@Column(name = "AUTHZD_CTR_NAME")
	private String authzdCtrName;

	@Column(name = "AUTHZD_CTR_ACCT_NO")
	private String authzdCtrAcctNo;
	
	@Column(name = "TRX_PRICE")
	private BigDecimal trxPrice;

	/**
	 * @return the jnlNo
	 */
	public String getJnlNo() {
		return jnlNo;
	}

	/**
	 * @param jnlNo the jnlNo to set
	 */
	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the acctNo
	 */
	public String getAcctNo() {
		return acctNo;
	}

	/**
	 * @param acctNo the acctNo to set
	 */
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	/**
	 * @return the pkgId
	 */
	public String getPkgId() {
		return pkgId;
	}

	/**
	 * @param pkgId the pkgId to set
	 */
	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	/**
	 * @return the trxType
	 */
	public String getTrxType() {
		return trxType;
	}

	/**
	 * @param trxType the trxType to set
	 */
	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	/**
	 * @return the trxDt
	 */
	public Date getTrxDt() {
		return trxDt;
	}

	/**
	 * @param trxDt the trxDt to set
	 */
	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	/**
	 * @return the trxAmt
	 */
	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	/**
	 * @param trxAmt the trxAmt to set
	 */
	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	/**
	 * @return the authzdCtrId
	 */
	public String getAuthzdCtrId() {
		return authzdCtrId;
	}

	/**
	 * @param authzdCtrId the authzdCtrId to set
	 */
	public void setAuthzdCtrId(String authzdCtrId) {
		this.authzdCtrId = authzdCtrId;
	}

	/**
	 * @return the authzdCtrName
	 */
	public String getAuthzdCtrName() {
		return authzdCtrName;
	}

	/**
	 * @param authzdCtrName the authzdCtrName to set
	 */
	public void setAuthzdCtrName(String authzdCtrName) {
		this.authzdCtrName = authzdCtrName;
	}

	/**
	 * @return the authzdCtrAcctNo
	 */
	public String getAuthzdCtrAcctNo() {
		return authzdCtrAcctNo;
	}

	/**
	 * @param authzdCtrAcctNo the authzdCtrAcctNo to set
	 */
	public void setAuthzdCtrAcctNo(String authzdCtrAcctNo) {
		this.authzdCtrAcctNo = authzdCtrAcctNo;
	}

	public BigDecimal getTrxPrice() {
		return trxPrice;
	}

	public void setTrxPrice(BigDecimal trxPrice) {
		this.trxPrice = trxPrice;
	}

	
	
	
}
