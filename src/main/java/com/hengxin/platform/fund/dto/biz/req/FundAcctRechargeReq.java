package com.hengxin.platform.fund.dto.biz.req;

import java.math.BigDecimal;
import java.util.Date;

public class FundAcctRechargeReq {
	
	/**
	 * 事件编号
	 */
	private String eventId;
	
	/**
	 * 充值会员编号
	 */
	private String userId;
	
	/**
	 * 充值会员名
	 */
	private String userName;
	
	/**
	 * 是否签约会员
	 */
	private String signedFlg;
	
	private String bankCode;
	
	private String cashPool;
	
	private String bankAcctNo;
	
	private String bankAcctName;
	
	/**
	 * 充值金额
	 */
	private BigDecimal trxAmt;
	
	/**
	 * 备注
	 */
	private String trxMemo;
	
	/**
	 * 当前操作用户
	 */
	private String currOpid;
	
	/**
	 * 当前工作日期
	 */
	private Date workDate;
	
	/**
	 * 关联银行流水号
	 */
	private String relBnkId;
	
	/**
	 * 关联业务编号
	 */
	private String bizId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getCurrOpid() {
		return currOpid;
	}

	public void setCurrOpid(String currOpid) {
		this.currOpid = currOpid;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	
    public String getRelBnkId() {
        return relBnkId;
    }
	
    public void setRelBnkId(String relBnkId) {
        this.relBnkId = relBnkId;
    }

	public String getSignedFlg() {
		return signedFlg;
	}

	public void setSignedFlg(String signedFlg) {
		this.signedFlg = signedFlg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAcctNo() {
		return bankAcctNo;
	}

	public void setBankAcctNo(String bankAcctNo) {
		this.bankAcctNo = bankAcctNo;
	}

	public String getBankAcctName() {
		return bankAcctName;
	}

	public void setBankAcctName(String bankAcctName) {
		this.bankAcctName = bankAcctName;
	}

	public String getCashPool() {
		return cashPool;
	}

	public void setCashPool(String cashPool) {
		this.cashPool = cashPool;
	}
	
}
