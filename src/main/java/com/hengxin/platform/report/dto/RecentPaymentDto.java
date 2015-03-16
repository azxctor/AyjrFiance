package com.hengxin.platform.report.dto;

import com.hengxin.platform.product.enums.EPayStatus;

public class RecentPaymentDto extends TimeSelectDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 还款状态.
     */
	private EPayStatus payStatus;
	/**
	 * 签约日期.
	 */
    private String signingStartDate;
    
    private String signingEndDate;

    public EPayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(EPayStatus payStatus) {
        this.payStatus = payStatus;
    }

	public String getSigningStartDate() {
		return signingStartDate;
	}

	public void setSigningStartDate(String signingStartDate) {
		this.signingStartDate = signingStartDate;
	}

	public String getSigningEndDate() {
		return signingEndDate;
	}

	public void setSigningEndDate(String signingEndDate) {
		this.signingEndDate = signingEndDate;
	}

}
