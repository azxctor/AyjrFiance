package com.hengxin.platform.fund.dto.biz.req.atom;

import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ESubAcctType;

public class InternalAcctTransferInfo {

	private String eventId;

	private String userId;

	private EFundUseType useType;

	private ESubAcctType fromAcctType;

	private ESubAcctType toAcctType;

	private BigDecimal trxAmt;

	private String trxMemo;

	private String bizId;

	private String currOpId;

	private Date workDate;

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

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public ESubAcctType getFromAcctType() {
		return fromAcctType;
	}

	public void setFromAcctType(ESubAcctType fromAcctType) {
		this.fromAcctType = fromAcctType;
	}

	public ESubAcctType getToAcctType() {
		return toAcctType;
	}

	public void setToAcctType(ESubAcctType toAcctType) {
		this.toAcctType = toAcctType;
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

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

}
