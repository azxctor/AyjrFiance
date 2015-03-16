package com.hengxin.platform.report.dto;

import com.hengxin.platform.report.enums.EAssetLevel;

public class InvestorBusinessTraceDto extends CommonParameter {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 会员等级.
	 */
	private String memberLevel;
    private String applicationStatus; // 状态
    private EAssetLevel remainLevel; // 余额等级
    private EAssetLevel assetLevel; // 总额等级
    private String startDate; // 开户起始日期
    private String endDate; // 开始截止日期
    private String serviceCenterId;// 授权服务中心userId

    public String getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}

	public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public EAssetLevel getRemainLevel() {
        return remainLevel;
    }

    public void setRemainLevel(EAssetLevel remainLevel) {
        this.remainLevel = remainLevel;
    }

    public EAssetLevel getAssetLevel() {
        return assetLevel;
    }

    public void setAssetLevel(EAssetLevel assetLevel) {
        this.assetLevel = assetLevel;
    }

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

	public String getServiceCenterId() {
		return serviceCenterId;
	}

	public void setServiceCenterId(String serviceCenterId) {
		this.serviceCenterId = serviceCenterId;
	}

}
