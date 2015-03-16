package com.hengxin.platform.report.dto;

import com.hengxin.platform.product.enums.EFeePayMethodType;

public class ServiceFeeDto extends CommonParameter {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EFeePayMethodType feePayMethodType;
    private String term;
    private String selectMonth;

    public EFeePayMethodType getFeePayMethodType() {
        return feePayMethodType;
    }

    public void setFeePayMethodType(EFeePayMethodType feePayMethodType) {
        this.feePayMethodType = feePayMethodType;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getSelectMonth() {
        return selectMonth;
    }

    public void setSelectMonth(String selectMonth) {
        this.selectMonth = selectMonth;
    }

}
