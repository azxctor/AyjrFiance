package com.hengxin.platform.report.dto;

public class AnotherTimeSelectDto extends TimeSelectDto {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String singStartDate; // 签约日期开始时间
    private String signEndDate; // 签约日期结束时间

    public String getSingStartDate() {
        return singStartDate;
    }

    public void setSingStartDate(String singStartDate) {
        this.singStartDate = singStartDate;
    }

    public String getSignEndDate() {
        return signEndDate;
    }

    public void setSignEndDate(String signEndDate) {
        this.signEndDate = signEndDate;
    }

}
