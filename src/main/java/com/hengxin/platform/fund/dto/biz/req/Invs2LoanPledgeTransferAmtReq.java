package com.hengxin.platform.fund.dto.biz.req;

import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFundUseType;

public class Invs2LoanPledgeTransferAmtReq {

	/**
	 * 事件编号
	 */
	private String eventId;
	
	/**
	 * 放款对应的冻结日志流水号
	 */
	private String frzJnlNo;
	
	/**
	 * 会员编号
	 */
	private String userId;
	
	/**
	 * 资金用途类型
	 */
	private EFundUseType useType;
	
	/**
	 * 贷款金额
	 */
	private BigDecimal loanAmt;
	
	/**
	 * 最大可提现金额
	 */
	private BigDecimal maxCashableAmt;
	
	/**
	 * 做投转贷拥有有效债权总金额
	 */
	private BigDecimal ownAmt;
	
	/**
	 * 交易备注
	 */
	private String trxMemo;
	
	/**
	 * 业务编号
	 */
	private String bizId;
	
	/**
	 * 当前操作用户
	 */
	private String currOpId;
	
	/**
	 * 当前工作日期
	 */
	private Date workDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
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

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getFrzJnlNo() {
		return frzJnlNo;
	}

	public void setFrzJnlNo(String frzJnlNo) {
		this.frzJnlNo = frzJnlNo;
	}

	public BigDecimal getLoanAmt() {
		return loanAmt;
	}

	public void setLoanAmt(BigDecimal loanAmt) {
		this.loanAmt = loanAmt;
	}

	public BigDecimal getMaxCashableAmt() {
		return maxCashableAmt;
	}

	public void setMaxCashableAmt(BigDecimal maxCashableAmt) {
		this.maxCashableAmt = maxCashableAmt;
	}

	public BigDecimal getOwnAmt() {
		return ownAmt;
	}

	public void setOwnAmt(BigDecimal ownAmt) {
		this.ownAmt = ownAmt;
	}
	
	
	
}
