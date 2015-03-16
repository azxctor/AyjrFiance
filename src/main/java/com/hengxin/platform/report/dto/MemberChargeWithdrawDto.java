package com.hengxin.platform.report.dto;

import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;
import com.hengxin.platform.report.enums.EFundUserSearchType;

public class MemberChargeWithdrawDto extends TimeSelectDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ECashPool fundChannel; // 渠道
    private String isSign; // 是否签约
    private EFundUserSearchType fundUserType; // 类型
    private ERechargeWithdrawFlag rechargeWithdrawFlag; // 充值类型

    public ECashPool getFundChannel() {
        return fundChannel;
    }

    public void setFundChannel(ECashPool fundChannel) {
        this.fundChannel = fundChannel;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public EFundUserSearchType getFundUserType() {
        return fundUserType;
    }

    public void setFundUserType(EFundUserSearchType fundUserType) {
        this.fundUserType = fundUserType;
    }

    public ERechargeWithdrawFlag getRechargeWithdrawFlag() {
        return rechargeWithdrawFlag;
    }

    public void setRechargeWithdrawFlag(ERechargeWithdrawFlag rechargeWithdrawFlag) {
        this.rechargeWithdrawFlag = rechargeWithdrawFlag;
    }

}
