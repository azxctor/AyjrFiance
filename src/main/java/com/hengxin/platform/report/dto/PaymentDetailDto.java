package com.hengxin.platform.report.dto;

import com.hengxin.platform.product.enums.EPayStatus;

public class PaymentDetailDto extends CommonParameter {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EPayStatus payStatus;
    private String payDate;

    public EPayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(EPayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

}
