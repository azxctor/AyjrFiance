package com.hengxin.platform.report.dto;

import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.report.enums.EPayDateType;

public class PaymentStatisticsDto extends TimeSelectDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EPayStatus payStatus;
    private EPayDateType payDateType;

    public EPayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(EPayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public EPayDateType getPayDateType() {
        return payDateType;
    }

    public void setPayDateType(EPayDateType payDateType) {
        this.payDateType = payDateType;
    }

}
