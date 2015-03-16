package com.hengxin.platform.escrow.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

public class EswRechargeOrderDto {
	
	private String orderId;

	private Date trxDt;

	private String acctNo;

	private String userId;

	private String eswCustAcctNo;

	private String eswAcctNo;

	private String eswSubAcctNo;

	private String eswUserNo;

	private BigDecimal trxAmt;

	private String trxMemo;

	private String openBankId;

	private String ordType;

	private String reqSignVal;

	private EOrderStatusEnum status;

	private String retCode;

	private String retMsg;

	private Date respTs;

	private String vouchNo;

	private String trxNo;

	private String transDt;

	private String transTm;

	private String respSignVal;

	private String createOpId;

	private Date createTs;

	private String lastMntOpid;

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

	public String getOpenBankId() {
		return openBankId;
	}

	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}

	public String getOrdType() {
		return ordType;
	}

	public void setOrdType(String ordType) {
		this.ordType = ordType;
	}

	public String getReqSignVal() {
		return reqSignVal;
	}

	public void setReqSignVal(String reqSignVal) {
		this.reqSignVal = reqSignVal;
	}

	public EOrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(EOrderStatusEnum status) {
		this.status = status;
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

	public Date getRespTs() {
		return respTs;
	}

	public void setRespTs(Date respTs) {
		this.respTs = respTs;
	}

	public String getVouchNo() {
		return vouchNo;
	}

	public void setVouchNo(String vouchNo) {
		this.vouchNo = vouchNo;
	}

	public String getTrxNo() {
		return trxNo;
	}

	public void setTrxNo(String trxNo) {
		this.trxNo = trxNo;
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

	public String getRespSignVal() {
		return respSignVal;
	}

	public void setRespSignVal(String respSignVal) {
		this.respSignVal = respSignVal;
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

	public String getLastMntOpid() {
		return lastMntOpid;
	}

	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}
	
}
