package com.hengxin.platform.member.dto;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class InvestorSearchDto extends DataTablesRequestDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String investorName;
	
	private String accountNo;
	
	private String serviceCenterName;

	/**
	 * @return the investorName
	 */
	public String getInvestorName() {
		return investorName;
	}

	/**
	 * @param investorName the investorName to set
	 */
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}

	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}

	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	/**
	 * @return the serviceCenterName
	 */
	public String getServiceCenterName() {
		return serviceCenterName;
	}

	/**
	 * @param serviceCenterName the serviceCenterName to set
	 */
	public void setServiceCenterName(String serviceCenterName) {
		this.serviceCenterName = serviceCenterName;
	}
	
}
