package com.hengxin.platform.fund.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.fund.enums.EFundUseType;

public class FundTrxJnlDto {
	
	/**
	 * 交易流水
	 */
	private String jnlNo;
	/**
	 * 事件编号
	 */
	private String eventId;
	/**
	 * 用户编号
	 */
	private String userId;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 账户号
	 */
	private String acctNo;
	/**
	 * 子账户编号
	 */
	private String subAcctNo;
	/**
	 * 资金用途
	 */
	private EFundUseType useType;
	/**
	 * 交易日期
	 */
	private Date trxDt;
	/**
	 * 资金收付类型
	 */
	private EFundPayRecvFlag payRecvFlg;
	/**
	 * 金额
	 */
	private BigDecimal trxAmt;
	/**
	 * 备注
	 */
	private String trxMemo;
	/**
	 * 资金池
	 */
    private ECashPool cashPool;
    /**
     * 余额
     */
    private BigDecimal bal;
	/**
	 * 关联业务编号
	 */
	private String relBizId;
	/**
	 * 创建用户编号
	 */
	private String createOpid;
	/**
	 * 创建时间
	 */
	private Date createTs;

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getSubAcctNo() {
		return subAcctNo;
	}

	public void setSubAcctNo(String subAcctNo) {
		this.subAcctNo = subAcctNo;
	}

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public EFundPayRecvFlag getPayRecvFlg() {
		return payRecvFlg;
	}

	public void setPayRecvFlg(EFundPayRecvFlag payRecvFlg) {
		this.payRecvFlg = payRecvFlg;
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

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}

	public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}

	public String getRelBizId() {
		return relBizId;
	}

	public void setRelBizId(String relBizId) {
		this.relBizId = relBizId;
	}

	public String getCreateOpid() {
		return createOpid;
	}

	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}
	
}
