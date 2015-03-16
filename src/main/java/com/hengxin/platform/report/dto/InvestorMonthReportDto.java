package com.hengxin.platform.report.dto;

import com.hengxin.platform.member.enums.EApplicationStatus;

public class InvestorMonthReportDto extends CommonParameter {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EApplicationStatus applicationStatus; // 状态

    public EApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(EApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

}
