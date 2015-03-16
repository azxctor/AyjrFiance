package com.hengxin.platform.report.dto;

import com.hengxin.platform.report.enums.ECreditTransferFeeType;

public class ECreditTransferFeeDto extends TimeSelectDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ECreditTransferFeeType creditTransferFeeType; // 关键字类型

    public ECreditTransferFeeType getCreditTransferFeeType() {
        return creditTransferFeeType;
    }

    public void setCreditTransferFeeType(ECreditTransferFeeType creditTransferFeeType) {
        this.creditTransferFeeType = creditTransferFeeType;
    }

}
