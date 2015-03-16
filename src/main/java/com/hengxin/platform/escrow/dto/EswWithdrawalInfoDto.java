package com.hengxin.platform.escrow.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

/**
 * 提现查询展示信息
 * 
 * @author chenwulou
 *
 */
public class EswWithdrawalInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// 用户平台的ID
	private String userId;
	
	private String acctNo;

	// 指令流水号
	private String orderId;

	// 提现时间
	private Date trxDt;

	// 提现金额
	private BigDecimal trxAmt;

	// 银行卡号
	private String bankCardNo;

	// 银行账户名
	private String bankCardName;

	// 状态
	private EOrderStatusEnum status;

	// 备注
	private String trxMemo;

	// 提现记录标识（第三方托管账户处Id）
	private String cashId;
	
	/** 查询返回信息**/
	
	// 返回码
	private String retCode;

	// 返回信息
	private String retMsg;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public EOrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
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

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public String getCashId() {
		return cashId;
	}

	public void setCashId(String cashId) {
		this.cashId = cashId;
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

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

}
