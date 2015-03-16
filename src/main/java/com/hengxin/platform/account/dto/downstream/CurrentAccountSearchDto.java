package com.hengxin.platform.account.dto.downstream;

import java.io.Serializable;

import com.hengxin.platform.fund.enums.EAcctType;

/**
 * 
 * @author jishen
 *
 */
public class CurrentAccountSearchDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 账户号
	 */
	private String acctNo;
	
	/**
	 * 账户类型
	 */
	private EAcctType acctType;

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public EAcctType getAcctType() {
		return acctType;
	}

	public void setAcctType(EAcctType acctType) {
		this.acctType = acctType;
	}
}
