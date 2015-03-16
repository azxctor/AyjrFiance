package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PackageAmount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal residualPrincipalAmt;
	private BigDecimal residualInterestAmt;
	private BigDecimal residualPrincipalAndInterestAmt;
	private Integer remTermLength;

	public BigDecimal getResidualPrincipalAmt() {
		return residualPrincipalAmt;
	}

	public void setResidualPrincipalAmt(BigDecimal residualPrincipalAmt) {
		this.residualPrincipalAmt = residualPrincipalAmt;
	}

	public BigDecimal getResidualInterestAmt() {
		return residualInterestAmt;
	}

	public void setResidualInterestAmt(BigDecimal residualInterestAmt) {
		this.residualInterestAmt = residualInterestAmt;
	}

	public BigDecimal getResidualPrincipalAndInterestAmt() {
		return residualPrincipalAndInterestAmt;
	}

	public void setResidualPrincipalAndInterestAmt(BigDecimal residualPrincipalAndInterestAmt) {
		this.residualPrincipalAndInterestAmt = residualPrincipalAndInterestAmt;
	}

	public Integer getRemTermLength() {
		return remTermLength;
	}

	public void setRemTermLength(Integer remTermLength) {
		this.remTermLength = remTermLength;
	}
}
