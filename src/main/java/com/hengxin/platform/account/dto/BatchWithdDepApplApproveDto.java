package com.hengxin.platform.account.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Class Name: BatchWithdDepApplApproveDto Description: TODO
 * 
 * @author jishen
 * 
 */

public class BatchWithdDepApplApproveDto implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date fromDate;
    
    private Date endDate;
    
    private String bnkCd;

	public String getBnkCd() {
		return bnkCd;
	}

	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

}
