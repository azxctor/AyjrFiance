package com.hengxin.platform.product.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CreditorTransferPackageRulePK implements Serializable {
    private Integer ruleId; // 规则Id
    private Integer squenceId; // 序号

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

}
