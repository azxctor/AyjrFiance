package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Name: currentAccount
 * 
 * @author jishen
 * 
 */
public class TransferAccountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 银行账户名
	 */
	private String bnkAcctName;
	
	/**
	 * 开户行全称
	 */
	private String bnkName;
	
	/**
	 * 银行账户号
	 */
	private String bnkAcctNo;
	
	/**
	 * 平台活期账户可用余额
	 */
	private BigDecimal availableBal;
	
	/**
	 * 会员Id
	 */
	private String userId;

	public String getBnkAcctName() {
		return bnkAcctName;
	}

	public void setBnkAcctName(String bnkAcctName) {
		this.bnkAcctName = bnkAcctName;
	}

	public String getBnkName() {
		return bnkName;
	}

	public void setBnkName(String bnkName) {
		this.bnkName = bnkName;
	}

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}

	public BigDecimal getAvailableBal() {
		return availableBal;
	}

	public void setAvailableBal(BigDecimal availableBal) {
		this.availableBal = availableBal;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
