package com.hengxin.platform.product.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class CreditorTransferRuleDto implements Serializable {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer ruleId; // Id
   
    private String overdueFlag; // 是否逾期

    @Min(value =0, message = "{package.transfer.overdue.maxday}")
    @Max(value =99, message = "{package.transfer.overdue.maxday}")
    private Integer maxOverdueDate; // 正在逾期

    @Min(value =0, message = "{package.transfer.overdue.mincount}")
    @Max(value =99, message = "{package.transfer.overdue.mincount}")
    private Integer maxOverdueCount; // 已逾期次数
    
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
