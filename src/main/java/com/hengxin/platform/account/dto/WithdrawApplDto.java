package com.hengxin.platform.account.dto;

import java.io.Serializable;

/**
 * Class Name: userInfo
 * 
 * @author jishen
 * 
 */
public class WithdrawApplDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bnkAcctNo;
	
	private String bnkAcctName;
	
	private String bnkName;

	private String bnkFullName;

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}

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

	public String getBnkFullName() {
		return bnkFullName;
	}

	public void setBnkFullName(String bnkFullName) {
		this.bnkFullName = bnkFullName;
	}
	
}
