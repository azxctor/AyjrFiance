package com.hengxin.platform.report.dto;

public class TimeSelectDto extends CommonParameter {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String startDate; // 开户起始日期
    private String endDate; // 开始截止日期

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
