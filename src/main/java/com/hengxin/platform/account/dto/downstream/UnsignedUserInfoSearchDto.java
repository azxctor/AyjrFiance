package com.hengxin.platform.account.dto.downstream;

import java.io.Serializable;

/**
 * 
 * @author jishen
 *
 */
public class UnsignedUserInfoSearchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 综合账号
	 */
	private String acctNo;
	
	/**
	 * 银行账户号
	 */
	private String bnkAcctNo;
	
	/**
	 * 银行开户名
	 */
	private String bnkAcctName; 

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getBnkAcctName() {
		return bnkAcctName;
	}

	public void setBnkAcctName(String bnkAcctName) {
		this.bnkAcctName = bnkAcctName;
	}

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}
}
