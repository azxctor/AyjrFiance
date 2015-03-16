package com.hengxin.platform.fund.dto.biz.req.atom;

import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFundUseType;

public class ReserveReq {

	/**
	 * 会员编号
	 */
	private String userId;
	
	/**
	 * 资金用途
	 */
	private EFundUseType useType;
	
	/**
	 * 活期余额不足的时候，是否使用小微宝余额
	 */
	private boolean isAddXwb;
	
	/**
	 * 保留金额
	 */
	private BigDecimal trxAmt;
	
	/**
	 * 备注
	 */
	private String trxMemo;
	
	/**
	 * 关联业务编号
	 */
	private String bizId;
	
	/**
	 * 当前操作用户id
	 */
	private String currOpId;
	
	/**
	 * 当前工作日期
	 */
	private  Date workDate;

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
	
	@Override
	public String toString() {
		return super.toString();
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public boolean isAddXwb() {
		return isAddXwb;
	}

	public void setAddXwb(boolean isAddXwb) {
		this.isAddXwb = isAddXwb;
	}
}
