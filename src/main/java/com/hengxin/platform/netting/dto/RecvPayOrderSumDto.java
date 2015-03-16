package com.hengxin.platform.netting.dto;

import java.math.BigDecimal;

public class RecvPayOrderSumDto {
	
	private BigDecimal totalPayAmt;
	
	private BigDecimal totalRecvAmt;

	public BigDecimal getTotalPayAmt() {
		return totalPayAmt;
	}

	public void setTotalPayAmt(BigDecimal totalPayAmt) {
		this.totalPayAmt = totalPayAmt;
	}

	public BigDecimal getTotalRecvAmt() {
		return totalRecvAmt;
	}

	public void setTotalRecvAmt(BigDecimal totalRecvAmt) {
		this.totalRecvAmt = totalRecvAmt;
	}
}
