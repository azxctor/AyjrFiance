package com.hengxin.platform.fund.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  Class Name: BankTrxJnl
 * 
 * @author liudc
 *
 */
@SuppressWarnings("serial")
public class BankTrxJnl implements Serializable{

	private String jnlNo;
	
	private String bnkCd;
	
	private String userId;
	
	private String userName;
	
	private String signedFlg;
	
	private String acctNo;
	
	private String subAcctNo;
	
	private String payRecvFlg;
	
	private String trxStatus;
	
	private Date trxDt;
	
	private BigDecimal trxAmt;
	
	private Date rvsDt;
	
	private String rvsJnlNo;
	
    private String cashPool;
	
	private String trxMemo;
	
	private String eventId;
	
	private String relBnkId;
	
	private String createOpid;
	
	private Date createTs;
	
	private String lastMntOpid;
	
	private Date lastMntTs;

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getBnkCd() {
		return bnkCd;
	}

	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
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

	public String getSignedFlg() {
		return signedFlg;
	}

	public void setSignedFlg(String signedFlg) {
		this.signedFlg = signedFlg;
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

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
	}

	public String getRelBnkId() {
		return relBnkId;
	}

	public void setRelBnkId(String relBnkId) {
		this.relBnkId = relBnkId;
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

	public String getPayRecvFlg() {
		return payRecvFlg;
	}

	public void setPayRecvFlg(String payRecvFlg) {
		this.payRecvFlg = payRecvFlg;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	public String getCashPool() {
		return cashPool;
	}

	public void setCashPool(String cashPool) {
		this.cashPool = cashPool;
	}

	public String getTrxStatus() {
		return trxStatus;
	}

	public void setTrxStatus(String trxStatus) {
		this.trxStatus = trxStatus;
	}

	public Date getRvsDt() {
		return rvsDt;
	}

	public void setRvsDt(Date rvsDt) {
		this.rvsDt = rvsDt;
	}

	public String getRvsJnlNo() {
		return rvsJnlNo;
	}

	public void setRvsJnlNo(String rvsJnlNo) {
		this.rvsJnlNo = rvsJnlNo;
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
