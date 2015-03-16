package com.hengxin.platform.fund.dto.biz.req;

import java.math.BigDecimal;

import com.hengxin.platform.fund.enums.EFeeType;

public class TradeFeePayReq {
	
	private EFeeType feeType;
	
	private BigDecimal trxAmt;
	
	private String trxMemo;

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public EFeeType getFeeType() {
		return feeType;
	}

	public void setFeeType(EFeeType feeType) {
		this.feeType = feeType;
	}
	
}
