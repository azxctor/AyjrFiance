package com.hengxin.platform.fund.dto.biz.req.atom;

import java.math.BigDecimal;

import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundUseType;

public class TransferInfo {
	
	/**
	 * 会员编号
	 */
	private String userId;
	
	/**
	 * 交易金额
	 */
	private BigDecimal trxAmt;
	
	/**
	 * 交易备注
	 */
	private String trxMemo;
	
	/**
	 * 资金用途
	 */
	private EFundUseType useType;
	
	/**
	 * 资金用途
	 */
	private ECashPool cashPool;
	
	/**
	 * 是否关联债权
	 */
	private boolean isRelZQ;
	
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

	public boolean isRelZQ() {
		return isRelZQ;
	}

	public void setRelZQ(boolean isRelZQ) {
		this.isRelZQ = isRelZQ;
	}

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	
}
