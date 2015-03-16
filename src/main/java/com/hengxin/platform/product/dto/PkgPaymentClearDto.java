package com.hengxin.platform.product.dto;

import java.math.BigDecimal;

import com.hengxin.platform.product.enums.EPkgPaymentClearType;

public class PkgPaymentClearDto {
	
	/**
	 * 融资包编号
	 */
	private String packageId;
	
	/**
	 * 还款期数
	 */
	private Integer period;
	
	/**
	 * 清分金额
	 */
	private BigDecimal clearAmt;
	
	/**
	 * 清分类型
	 */
	private EPkgPaymentClearType clearType;

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public BigDecimal getClearAmt() {
		return clearAmt;
	}

	public void setClearAmt(BigDecimal clearAmt) {
		this.clearAmt = clearAmt;
	}

	public EPkgPaymentClearType getClearType() {
		return clearType;
	}

	public void setClearType(EPkgPaymentClearType clearType) {
		this.clearType = clearType;
	}
	
}
