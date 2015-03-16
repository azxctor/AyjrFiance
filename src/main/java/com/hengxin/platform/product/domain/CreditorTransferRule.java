package com.hengxin.platform.product.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "FP_TSFR_RULE")
public class CreditorTransferRule extends BaseInfo implements Serializable {
    @Id
    @Column(name = "RULE_ID")
    private Integer ruleId; // Id

    @Column(name = "OVERDUE_FLG")
    private String overdueFlag; // 是否逾期

    @Column(name = "MAX_OVERDUE_DAT_CT")
    private Integer maxOverdueDate; // 正在逾期

    @Column(name = "MAX_OVERDUE_CT")
    private Integer maxOverdueCount; // 已逾期次数
    
    @Column(name = "MIN_TERM_DAYS")
    private Integer minTermDays; // 融资包期限

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getOverdueFlag() {
        return overdueFlag;
    }

    public void setOverdueFlag(String overdueFlag) {
        this.overdueFlag = overdueFlag;
    }

    public Integer getMaxOverdueDate() {
        return maxOverdueDate;
    }

    public void setMaxOverdueDate(Integer maxOverdueDate) {
        this.maxOverdueDate = maxOverdueDate;
    }

    public Integer getMaxOverdueCount() {
        return maxOverdueCount;
    }

    public void setMaxOverdueCount(Integer maxOverdueCount) {
        this.maxOverdueCount = maxOverdueCount;
    }

	public Integer getMinTermDays() {
		return minTermDays;
	}

	public void setMinTermDays(Integer minTermDays) {
		this.minTermDays = minTermDays;
	}

}
