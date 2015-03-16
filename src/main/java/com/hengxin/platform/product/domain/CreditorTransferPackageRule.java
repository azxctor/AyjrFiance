package com.hengxin.platform.product.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@IdClass(CreditorTransferPackageRulePK.class)
@Table(name = "FP_TSFR_PKG_RULE")
public class CreditorTransferPackageRule extends BaseInfo implements Serializable {
    @Id
    @Column(name = "RULE_ID")
    private Integer ruleId; // 规则Id

    @Id
    @Column(name = "SEQ_ID")
    private Integer squenceId; // 序号

    @Column(name = "ALLOW_CD")
    private String allowType; // 类型区分是否是package类型或者packageId

    @Column(name = "PKG_TYPE")
    private String packageType;// package类型

    @Column(name = "PKG_ID")
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
