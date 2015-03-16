package com.hengxin.platform.fund.dto.biz.req;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现金额扣款请求
 * Class Name: FundAcctWithDrawReq
 * Description: 提现金额在审批通过后解保留并扣款的请求
 * @author jishen
 *
 */
public class FundAcctWithDrawReq {
	
	/**
	 * 事件编号
	 */
	private String eventId;
	
	/**
	 * 提现资金保留编号
	 */
	private String resvJnlNo;

	/**
	 * 会员编号
	 */
	private String userId;
	
	/**
	 * 会员名
	 */
	private String userName;
	
	/**
	 * 扣款金额
	 */
	private BigDecimal trxAmt;
	
	private String bankCode;
	
	private String cashPool;
	
	private String bnkAcctNo;
	
	private String bnkAcctName;
	
	/**
	 * 备注
	 */
	private String trxMemo;
	
	/**
	 * 保留日志流水号
	 */
	private String bizId;
	
	/**
	 * 当前操作用户
	 */
	private String currOpid;
	
	/**
	 * 当前工作日期
	 */
	private Date workDate;
	
	/**
	 * 是否签约会员
	 */
	private String signedFlg;
	
	/**
	 * 关联银行交易流水
	 */
	private String relBnkId;

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

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCashPool() {
		return cashPool;
	}

	public void setCashPool(String cashPool) {
		this.cashPool = cashPool;
	}

	public String getResvJnlNo() {
		return resvJnlNo;
	}

	public void setResvJnlNo(String resvJnlNo) {
		this.resvJnlNo = resvJnlNo;
	}

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}

	public String getBnkAcctName() {
		return bnkAcctName;
	}

	public void setBnkAcctName(String bnkAcctName) {
		this.bnkAcctName = bnkAcctName;
	}
}
