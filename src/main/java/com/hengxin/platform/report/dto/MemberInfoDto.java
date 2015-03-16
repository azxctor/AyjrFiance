package com.hengxin.platform.report.dto;

import com.hengxin.platform.report.enums.EMemberType;

public class MemberInfoDto extends TimeSelectDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status; // 状态
    private EMemberType memberType; // 类型
    private String regionCode; // 区域代码
    private String isSign;// 是否签约

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EMemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(EMemberType memberType) {
        this.memberType = memberType;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

}
