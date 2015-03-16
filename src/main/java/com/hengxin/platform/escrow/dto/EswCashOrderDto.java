package com.hengxin.platform.escrow.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

/**
 * 提现指令
 * 
 * */
public class EswCashOrderDto {

	// 指令流水号
	private String orderId;
	// 交易日期
	private Date trxDt;
	// 会员综合账户
	private String acctNo;
	// 会员编号
	private String userId;
	// 托管商户账号(商户托管账号)
	private String eswCustAcctNo;
	// 托管会员账号(钱包介质编号)
	private String eswAcctNo;
	// 托管子账户编号 (电子账户编号)
	private String eswSubAcctNo;
	// 托管会员编号
	private String eswUserNo;
	// 银行卡号
	private String bankCardNo;
	// 持卡人姓名
	private String bankCardName;
	// 状态
	private EOrderStatusEnum status;
	// 币种
	private String curr;
	// 提现金额
	private BigDecimal trxAmt;
	// 提现备注
	private String trxMemo;
	// 账户类型
	private String acctType;
	// 订单类型
	private String ordType;
	// 返回编码
	private String retCode;
	// 返回信息
	private String retMsg;
	// 反馈交易编号
	private String trxNo;
	// 账户余额
	private BigDecimal bal;
	// 提现记录标识
	private String cashId;
	// 订单创建用户ID
	private String createOpId;
	// 订单创建时间
	private Date createTs;
	// 更新用户
	private String lastMntOpId;
	// 更新时间
	private Date lastMntTs;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEswCustAcctNo() {
		return eswCustAcctNo;
	}

	public void setEswCustAcctNo(String eswCustAcctNo) {
		this.eswCustAcctNo = eswCustAcctNo;
	}

	public String getEswAcctNo() {
		return eswAcctNo;
	}

	public void setEswAcctNo(String eswAcctNo) {
		this.eswAcctNo = eswAcctNo;
	}

	public String getEswSubAcctNo() {
		return eswSubAcctNo;
	}

	public void setEswSubAcctNo(String eswSubAcctNo) {
		this.eswSubAcctNo = eswSubAcctNo;
	}

	public String getEswUserNo() {
		return eswUserNo;
	}

	public void setEswUserNo(String eswUserNo) {
		this.eswUserNo = eswUserNo;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankCardName() {
		return bankCardName;
	}

	public void setBankCardName(String bankCardName) {
		this.bankCardName = bankCardName;
	}

	public EOrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
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

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getOrdType() {
		return ordType;
	}

	public void setOrdType(String ordType) {
		this.ordType = ordType;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public String getTrxNo() {
		return trxNo;
	}

	public void setTrxNo(String trxNo) {
		this.trxNo = trxNo;
	}

	public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}

	public String getCashId() {
		return cashId;
	}

	public void setCashId(String cashId) {
		this.cashId = cashId;
	}

	public String getCreateOpId() {
		return createOpId;
	}

	public void setCreateOpId(String createOpId) {
		this.createOpId = createOpId;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public String getLastMntOpId() {
		return lastMntOpId;
	}

	public void setLastMntOpId(String lastMntOpId) {
		this.lastMntOpId = lastMntOpId;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

}
