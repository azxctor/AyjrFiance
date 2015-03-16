package com.hengxin.platform.account.dto;

import java.io.Serializable;

/**
 * Class Name: TransApplPriInfoSearchDto Description: TODO
 * 
 * @author jishen
 * 
 */

public class TransApplPriInfoSearchDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 资金划转审批编号
	 */
	private String applNo;

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
}
