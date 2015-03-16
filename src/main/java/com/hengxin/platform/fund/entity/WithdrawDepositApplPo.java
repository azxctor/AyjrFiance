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
 *  Class Name: AcctWdrwAppl
 * 
 * @author tingwang
 * 会员提现申请表
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AC_WDRW_APPL")
@EntityListeners(IdInjectionEntityListener.class)
public class WithdrawDepositApplPo implements Serializable{
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
	
	@Column(name="BNK_OPEN_PROV")
	private String bnkOpenProv;
	
	@Column(name="BNK_OPEN_CITY")
	private String bnkOpenCity;
	
	@Column(name="BNK_NAME")
	private String bnkName;
	
	@Column(name="BNK_OPEN_BRCH")
	private String bnkOpenBrch;
	
	@Temporal(TemporalType.DATE)
	@Column(name="TRX_DT")
	private Date trxDt;
	
	@Column(name="TRX_AMT")
	private BigDecimal trxAmt;
	
	@Column(name="TRX_MEMO")
	private String trxMemo;
	
	@Column(name="REM_FLG")
	private String remFlg;
	
	@Column(name="REL_FNR_JNL_NO")
	private String relFnrJnlNo;
	
	@Column(name="REL_BNK_JNL_NO")
	private String relBnkJnlNo;
	
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
	
	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}

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

	public String getBnkOpenBrch() {
		return bnkOpenBrch;
	}

	public void setBnkOpenBrch(String bnkOpenBrch) {
		this.bnkOpenBrch = bnkOpenBrch;
	}

	public void setRemFlg(String remFlg) {
		this.remFlg = remFlg;
	}

    public String getRelBnkJnlNo() {
        return relBnkJnlNo;
    }

    public void setRelBnkJnlNo(String relBnkJnlNo) {
        this.relBnkJnlNo = relBnkJnlNo;
    }

	public String getBnkCd() {
		return bnkCd;
	}

	public void setBnkCd(String bnkCd) {
		this.bnkCd = bnkCd;
	}

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}

}
