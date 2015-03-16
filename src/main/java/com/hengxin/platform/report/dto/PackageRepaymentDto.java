package com.hengxin.platform.report.dto;

import java.io.Serializable;

import com.hengxin.platform.product.enums.EPayStatus;

public class PackageRepaymentDto extends CommonParameter implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String signStartDate; // 签约开始日期
    private String signEndDate; // 签约结束日期
    private EPayStatus payStatus; // 还款状态

    // private String keyWord; // 关键字

    public String getSignStartDate() {
        return signStartDate;
    }

    public void setSignStartDate(String signStartDate) {
        this.signStartDate = signStartDate;
    }

    public String getSignEndDate() {
        return signEndDate;
    }

    public void setSignEndDate(String signEndDate) {
        this.signEndDate = signEndDate;
    }

    public EPayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(EPayStatus payStatus) {
        this.payStatus = payStatus;
    }

}
