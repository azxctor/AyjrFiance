package com.hengxin.platform.escrow.dto;

import java.math.BigDecimal;

public class AcctBalnceInfoDto {

	/* 第三方托管账户余额信息 */
	
	// 交易账号
	private String acctNo;

	// 返回代码 00 则查询正常，否则 表示异常
	private String retCode;

	// 返回代码
	private String retMsg;

	// 余额
	private BigDecimal balance;

	// 运营商
	private String servProv;

	// 托管账号
	private String eswAcctNo;

	// 托管子账号
	private String eswSubAcctNo;

	// 发卡行
	private String bankId;

	// 发卡行名称
	private String bankName;
	
	// 币种
	private String curr;

	private String isReal;

	/* 交易系统资金账户信息 */
	
	// 账户余额
	private BigDecimal acctBal;
	
	// 可用余额
	private BigDecimal avlAmt;
	
	// 可提现金额
	private BigDecimal cashAbleAmt;

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getServProv() {
		return servProv;
	}

	public void setServProv(String servProv) {
		this.servProv = servProv;
	}

	public String getEswAcctNo() {
		return eswAcctNo;
	}

	public void setEswAcctNo(String eswAcctNo) {
		this.eswAcctNo = eswAcctNo;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public String getIsReal() {
		return isReal;
	}

	public void setIsReal(String isReal) {
		this.isReal = isReal;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public String getEswSubAcctNo() {
		return eswSubAcctNo;
	}

	public void setEswSubAcctNo(String eswSubAcctNo) {
		this.eswSubAcctNo = eswSubAcctNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public BigDecimal getAcctBal() {
		return acctBal;
	}

	public void setAcctBal(BigDecimal acctBal) {
		this.acctBal = acctBal;
	}

	public BigDecimal getAvlAmt() {
		return avlAmt;
	}

	public void setAvlAmt(BigDecimal avlAmt) {
		this.avlAmt = avlAmt;
	}

	public BigDecimal getCashAbleAmt() {
		return cashAbleAmt;
	}

	public void setCashAbleAmt(BigDecimal cashAbleAmt) {
		this.cashAbleAmt = cashAbleAmt;
	}

}
