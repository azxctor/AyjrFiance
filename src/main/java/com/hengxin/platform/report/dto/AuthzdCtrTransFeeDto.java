package com.hengxin.platform.report.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AuthzdCtrTransFeeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 会员姓名
	 */
	private String userName;

	/**
	 * 交易账号
	 */
	private String acctNo;

	/**
	 * 融资包编号
	 */
	private String pkgId;

	/**
	 * 交易类型
	 */
	private String trxType;

	/**
	 * 交易日期
	 */
	private String trxDt;

	/**
	 * 转让价格
	 */
	private BigDecimal trxPrice;
	/**
	 * 转让手续费
	 */
	private BigDecimal trxAmt;

	/**
	 * 介绍人
	 */
	private String agentName;

	/**
	 * ‍授权服务中心ID
	 */
	private String authzdCtrId;
	
	/**
	 * ‍授权服务中心名称
	 */
	private String authzdCtrName;
	
	/**
	 * 授权编号
	 */
	private String authzdCtrAcctNo;
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
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
	 * @param acctNo
	 *            the acctNo to set
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
	 * @param pkgId
	 *            the pkgId to set
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
	 * @param trxType
	 *            the trxType to set
	 */
	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	/**
	 * @return the trxDt
	 */
	public String getTrxDt() {
		return trxDt;
	}

	/**
	 * @param trxDt
	 *            the trxDt to set
	 */
	public void setTrxDt(String trxDt) {
		this.trxDt = trxDt;
	}

	/**
	 * @return the trxAmt
	 */
	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	/**
	 * @param trxAmt
	 *            the trxAmt to set
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
	 * @param agentName
	 *            the agentName to set
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
	 * @param authzdCtrId
	 *            the authzdCtrId to set
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
	 * @param authzdCtrAcctNo
	 *            the authzdCtrAcctNo to set
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
