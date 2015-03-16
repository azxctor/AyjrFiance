package com.hengxin.platform.report.dto;

import java.io.Serializable;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

public class DailyRiskSearchDto extends DataTablesRequestDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String startDate; // 起始时间
    private String endDate; // 结束时间

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
