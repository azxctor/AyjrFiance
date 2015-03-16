package com.hengxin.platform.fund.dto.biz.req;

import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFeePeriodType;
import com.hengxin.platform.fund.enums.EFeeType;

public class UserPayFeeReq {
	
	private String eventId;
	
	private String userId;
	
	private EFeeType feeType;
	
	private EFeePeriodType periodType;
	
	private Integer periodNum;
	
	private BigDecimal trxAmt;
	
	private String trxMemo;
	
	private String currOpId;
	
	private Date workDate;
	
	private String bizId;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public EFeeType getFeeType() {
		return feeType;
	}

	public void setFeeType(EFeeType feeType) {
		this.feeType = feeType;
	}

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

	public String getCurrOpId() {
		return currOpId;
	}

	public void setCurrOpId(String currOpId) {
		this.currOpId = currOpId;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public EFeePeriodType getPeriodType() {
		return periodType;
	}

	public void setPeriodType(EFeePeriodType periodType) {
		this.periodType = periodType;
	}

	public Integer getPeriodNum() {
		return periodNum;
	}

	public void setPeriodNum(Integer periodNum) {
		this.periodNum = periodNum;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	
	
}
