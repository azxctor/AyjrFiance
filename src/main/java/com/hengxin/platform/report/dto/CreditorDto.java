package com.hengxin.platform.report.dto;

import com.hengxin.platform.report.enums.EDebtStatus;

public class CreditorDto extends TimeSelectDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EDebtStatus debtStatus;

    public EDebtStatus getDebtStatus() {
        return debtStatus;
    }

    public void setDebtStatus(EDebtStatus debtStatus) {
        this.debtStatus = debtStatus;
    }

}
