package com.hengxin.platform.escrow.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

/**
 * 充值查询Dto
 * @author juhuahuang
 *
 */
public class EswRechargreInfoDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private String orderId;
	private String trxDt;    		//交易日期	
	private String acctNo;			//会员综合账户	
	private BigDecimal	trxAmt;		//金额	
	private String trxMemo;		//交易备注	
	private String openBankId;		//	发卡行编号
	private EOrderStatusEnum status;			//指令状态
	private String retCode;			//返回编码	
	private String retMsg;			//返回信息	
	private String vouchNo;			//充值凭证号
	private String transDt;			//扣款日期	
	private String transTm;			//扣款时间
	
	public String getTrxMemo() {
		return trxMemo;
	}
	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}
	public String getTrxDt() {
		return trxDt;
	}
	public void setTrxDt(String trxDt) {
		this.trxDt = trxDt;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public BigDecimal getTrxAmt() {
		return trxAmt;
	}
	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}
	public String getOpenBankId() {
		return openBankId;
	}
	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public EOrderStatusEnum getStatus() {
		return status;
	}
	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public String getVouchNo() {
		return vouchNo;
	}
	public void setVouchNo(String vouchNo) {
		this.vouchNo = vouchNo;
	}
	public String getTransDt() {
		return transDt;
	}
	public void setTransDt(String transDt) {
		this.transDt = transDt;
	}
	public String getTransTm() {
		return transTm;
	}
	public void setTransTm(String transTm) {
		this.transTm = transTm;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
