package com.hengxin.platform.report.dto;

import com.hengxin.platform.product.enums.ETransferStatus;
import com.hengxin.platform.report.enums.ECreditTransferKeyType;
import com.hengxin.platform.report.enums.ECreditTransferTimeType;

public class CreditTransferReportDto extends TimeSelectDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ECreditTransferKeyType creditTransferKeyType; // 关键字类型
    private ECreditTransferTimeType creditTransferTimeType; // 日期类型
    private ETransferStatus transferStatus; // 债权转让状态

    public ECreditTransferKeyType getCreditTransferKeyType() {
        return creditTransferKeyType;
    }

    public void setCreditTransferKeyType(ECreditTransferKeyType creditTransferKeyType) {
        this.creditTransferKeyType = creditTransferKeyType;
    }

    public ECreditTransferTimeType getCreditTransferTimeType() {
        return creditTransferTimeType;
    }

    public void setCreditTransferTimeType(ECreditTransferTimeType creditTransferTimeType) {
        this.creditTransferTimeType = creditTransferTimeType;
    }

    public ETransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(ETransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

}
