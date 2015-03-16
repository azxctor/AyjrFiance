package com.hengxin.platform.report.dto;

import com.hengxin.platform.report.enums.EMemberStatisticsType;
import com.hengxin.platform.report.enums.ETimeType;

public class MemberStatisticsDto extends TimeSelectDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EMemberStatisticsType memberType;
    private ETimeType timeType;

    public EMemberStatisticsType getMemberType() {
        return memberType;
    }

    public void setMemberType(EMemberStatisticsType memberType) {
        this.memberType = memberType;
    }

    public ETimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(ETimeType timeType) {
        this.timeType = timeType;
    }

}
