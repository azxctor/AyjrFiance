package com.hengxin.platform.product.dto;

import java.io.Serializable;

public class CreditorTransferPackageRuleDto implements Serializable {   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer ruleId; // 规则Id

    private Integer squenceId; // 序号

    private String allowType; // 类型区分是否是package类型或者packageId, Y 是类型，N是包

    private String packageType;// package类型

    private String packageId;// packageId

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getSquenceId() {
        return squenceId;
    }

    public void setSquenceId(Integer squenceId) {
        this.squenceId = squenceId;
    }

    public String getAllowType() {
        return allowType;
    }

    public void setAllowType(String allowType) {
        this.allowType = allowType;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

}
