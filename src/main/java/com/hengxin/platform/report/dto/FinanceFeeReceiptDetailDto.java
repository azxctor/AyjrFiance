package com.hengxin.platform.report.dto;

import com.hengxin.platform.report.enums.EFeePayMethod;

public class FinanceFeeReceiptDetailDto extends TimeSelectDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EFeePayMethod feePayMethodType;

    public EFeePayMethod getFeePayMethodType() {
        return feePayMethodType;
    }

    public void setFeePayMethodType(EFeePayMethod feePayMethodType) {
        this.feePayMethodType = feePayMethodType;
    }

}
