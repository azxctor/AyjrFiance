package com.hengxin.platform.report.dto;

import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.report.enums.EMemberType;

public class MemberAssetDto extends CommonParameter {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EMemberType memberType; // 用户类型
    private ECashPool cashPool; // 渠道
    private String isSigned; // 是否签约，0是，1否
    private String availableAmount; // 可用余额
    private String withdrawAmount; // 提现金额
    private String frozenAmount; // 冻结金额

    public EMemberType getMemberType() {
        return memberType;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(String withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(String frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public void setMemberType(EMemberType memberType) {
        this.memberType = memberType;
    }

    public ECashPool getCashPool() {
        return cashPool;
    }

    public void setCashPool(ECashPool cashPool) {
        this.cashPool = cashPool;
    }

    public String getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(String isSigned) {
        this.isSigned = isSigned;
    }

}
