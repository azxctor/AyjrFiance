package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.fund.entity.converter.ECashPoolEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundApplStatusEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundDealStatusEnumConverter;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;

/**
 * 
 * @author dcliu
 * 
 * 会员充值申请
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AC_RECHARGE_APPL")
@EntityListeners(IdInjectionEntityListener.class)
public class RechargeApplPo implements Serializable{

	@Id
	@Column(name="APPL_NO")
	private String applNo;
	
	@Column(name="APPL_STATUS")
	@Convert(converter = FundApplStatusEnumConverter.class)
	private EFundApplStatus applStatus;
	
	@Column(name="APPR_OPID")
	private String apprOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APPR_TS")
	private Date apprTs;
	
	@Column(name="EVENT_ID")
	private String eventId;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Temporal(TemporalType.DATE)
	@Column(name="APPL_DT")
	private Date applDt;
	
	@Column(name="ACCT_NO")
	private String acctNo;
	
	@Column(name="SUB_ACCT_NO")
	private String subAcctNo;
    
    @Column(name = "CASH_POOL")
	@Convert(converter = ECashPoolEnumConverter.class)
    private ECashPool cashPool;
	
	@Column(name="DEAL_STATUS")
	@Convert(converter = FundDealStatusEnumConverter.class)
	private EFundDealStatus dealStatus;
	
	@Column(name="DEAL_MEMO")
	private String dealMemo;
	
	@Column(name="BNK_ACCT_NO")
	private String bnkAcctNo;
	
	@Column(name="BNK_ACCT_NAME")
	private String bnkAcctName;
	
	@Column(name = "BNK_CD")
	private String bnkCd;
	
	@Temporal(TemporalType.DATE)
	@Column(name="TRX_DT")
	private Date trxDt;
	
	@Column(name = "TRX_AMT")
	private BigDecimal trxAmt;
	
	@Column(name="TRX_MEMO")
	private String trxMemo;
	
	@Column(name="REM_FLG")
	private String remFlg;
	
	@Column(name="REL_BNK_JNL_NO")
	private String relBnkJnlNo;
	
	@Column(name="VOUCHER_NO")
	private String voucherNo;
	
	@Column(name="CREATE_OPID")
	private String createOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TS")
	private Date createTs;
	
	@Column(name="LAST_MNT_OPID")
	private String lastMntOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_MNT_TS")
	private Date lastMntTs;
	
	@Version
    @Column(name = "VERSION_CT")
	private Long versionCt;

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

	public String getBnkAcctNo() {
		return bnkAcctNo;
	}

	public void setBnkAcctNo(String bnkAcctNo) {
		this.bnkAcctNo = bnkAcctNo;
	}

	public String getBnkAcctName() {
		return bnkAcctName;
	}

	public void setBnkAcctName(String bnkAcctName) {
		this.bnkAcctName = bnkAcctName;
	}

	public String getBnkCd() {
		return bnkCd;
	}

	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
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

	public void setRemFlg(String remFlg) {
		this.remFlg = remFlg;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
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

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}

	public String getRelBnkJnlNo() {
		return relBnkJnlNo;
	}

	public void setRelBnkJnlNo(String relBnkJnlNo) {
		this.relBnkJnlNo = relBnkJnlNo;
	}

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}
	
}
