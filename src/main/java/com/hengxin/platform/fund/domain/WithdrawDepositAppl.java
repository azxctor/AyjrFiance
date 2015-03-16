package com.hengxin.platform.fund.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;

/**
 *  Class Name: AcctWdrwAppl
 * 
 * @author liudc
 *
 */
@SuppressWarnings("serial")
public class WithdrawDepositAppl implements Serializable{
	
	private String applNo;
	
	private EFundApplStatus applStatus;
	
	private String apprOpid;
	
	private Date apprTs;
	
	private String userId;
	
	private String userName;
	
	private Date applDt;
	
	private String acctNo;
	
	private String subAcctNo;
	
	private EFundDealStatus dealStatus;
	
	private String dealMemo;
	
	private String bnkAcctName;
	
	private String bnkAcctNo;
	
	private String bnkOpenProv;
	
	private String bnkOpenCity;
	
	private String bnkName;
	
	private String bnkOpenBrch;
	
	private Date trxDt;
	
	private BigDecimal trxAmt;
	
	private String trxMemo;
	
	private String remFlg;
	
	private String relFnrJnlNo;
	
	private String relBnkJnlNo;
	
	private String createOpid;
	
	private Date createTs;
	
	private String lastMntOpid;
	
	private Date lastMntTs;

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public EFundApplStatus getApplStatus() {
		return applStatus;
	}

	public void setApplStatus(EFundApplStatus applStatus) {
		this.applStatus = applStatus;
	}

	public String getApprOpid() {
		return apprOpid;
	}

	public void setApprOpid(String apprOpid) {
		this.apprOpid = apprOpid;
	}

	public Date getApprTs() {
		return apprTs;
	}

	public void setApprTs(Date apprTs) {
		this.apprTs = apprTs;
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

	public Date getApplDt() {
		return applDt;
	}

	public void setApplDt(Date applDt) {
		this.applDt = applDt;
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

	public EFundDealStatus getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(EFundDealStatus dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getDealMemo() {
		return dealMemo;
	}

	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}

	public String getBnkAcctName() {
		return bnkAcctName;
	}

	public void setBnkAcctName(String bnkAcctName) {
		this.bnkAcctName = bnkAcctName;
	}

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}

	public String getBnkOpenProv() {
		return bnkOpenProv;
	}

	public void setBnkOpenProv(String bnkOpenProv) {
		this.bnkOpenProv = bnkOpenProv;
	}

	public String getBnkOpenCity() {
		return bnkOpenCity;
	}

	public void setBnkOpenCity(String bnkOpenCity) {
		this.bnkOpenCity = bnkOpenCity;
	}

	public String getBnkName() {
		return bnkName;
	}

	public void setBnkName(String bnkName) {
		this.bnkName = bnkName;
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

	public String getRemFlg() {
		return remFlg;
	}

	public void setRemFlag(String remFlg) {
		this.remFlg = remFlg;
	}

	public String getRelFnrJnlNo() {
		return relFnrJnlNo;
	}

	public void setRelFnrJnlNo(String relFnrJnlNo) {
		this.relFnrJnlNo = relFnrJnlNo;
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

	public String getRelBnkJnlNo() {
		return relBnkJnlNo;
	}

	public void setRelBnkJnlNo(String relBnkJnlNo) {
		this.relBnkJnlNo = relBnkJnlNo;
	}

	public void setRemFlg(String remFlg) {
		this.remFlg = remFlg;
	}

	public String getBnkOpenBrch() {
		return bnkOpenBrch;
	}

	public void setBnkOpenBrch(String bnkOpenBrch) {
		this.bnkOpenBrch = bnkOpenBrch;
	}

}
