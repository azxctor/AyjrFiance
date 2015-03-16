package com.hengxin.platform.account.dto;

import java.io.Serializable;


/**
 * Class Name: PrintInfoSearchDto
 * Description: TODO
 * @author jishen
 *
 */

public class PrintInfoSearchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 充值/提现交易流水号
	 */
	private String jnlNo;
	
	/**
	 * 充值申请编号
	 */
	private String applNo; 

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
}
