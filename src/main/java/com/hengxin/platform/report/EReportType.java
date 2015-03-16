package com.hengxin.platform.report;

/**
 * enumerate type of report.
 * 
 * @author QiangWang (DT82271)
 */
public enum EReportType {
	AuthzdCtrTransFee("债权转让费用报表");
	
	private String title;
	
	private EReportType(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
