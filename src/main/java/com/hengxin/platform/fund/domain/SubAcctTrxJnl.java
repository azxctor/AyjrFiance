package com.hengxin.platform.fund.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Class Name: SubAcctTrxJnl
 * 
 * @author liudc
 * 
 */
@SuppressWarnings("serial")
public class SubAcctTrxJnl implements Serializable {

	private String jnlNo;
	
	private String eventId;
	
	private String acctNo;
	
	private String subAcctNo;
	
	private String trxType;
	
	private Date trxDt;
	
	private String payRecvFlg;
	
	private BigDecimal trxAmt;
	
	private String trxMemo;
	
    private String cashPool;
    
    private BigDecimal bal;
	
	private String relBizId;
	
	private String createOpid;
	
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

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public String getPayRecvFlg() {
		return payRecvFlg;
	}

	public void setPayRecvFlg(String payRecvFlg) {
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

	public String getCashPool() {
		return cashPool;
	}

	public void setCashPool(String cashPool) {
		this.cashPool = cashPool;
	}

	public BigDecimal getBal() {
		return bal;
	}

	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}
	
}
