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
import com.hengxin.platform.fund.entity.converter.EBankTypeEnumConverter;
import com.hengxin.platform.fund.entity.converter.EBnkTrxStatusEnumConverter;
import com.hengxin.platform.fund.entity.converter.ECashPoolEnumConverter;
import com.hengxin.platform.fund.entity.converter.EFlagTypeEnumConverter;
import com.hengxin.platform.fund.entity.converter.ERechargeWithdrawFlagEnumConverter;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.EBnkTrxStatus;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;

/**
 * Class Name: BankTrxJnlPo
 * 
 * @author tingwang 银行交易日志表
 */

@Entity
@SuppressWarnings("serial")
@Table(name = "AC_BANK_TRX_JNL")
@EntityListeners(IdInjectionEntityListener.class)
public class BankTrxJnlPo implements Serializable {
	@Id
	@Column(name = "JNL_NO")
	private String jnlNo;

	@Column(name = "EVENT_ID")
	private String eventId;

	@Column(name = "BNK_CD")
	@Convert(converter = EBankTypeEnumConverter.class)
	private EBankType bankCode;
	
	@Column(name = "BNK_ACCT_NO")
	private String bnkAcctNo;
	
	@Column(name = "BNK_ACCT_NAME")
	private String bnkAcctName;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "SIGNED_FLG")
	@Convert(converter = EFlagTypeEnumConverter.class)
	private EFlagType signedFlg;

	@Column(name = "ACCT_NO")
	private String acctNo;

	@Column(name = "SUB_ACCT_NO")
	private String subAcctNo;

	@Column(name = "PAY_RECV_FLG")
	@Convert(converter = ERechargeWithdrawFlagEnumConverter.class)
	private ERechargeWithdrawFlag rechargeWithdrawFlag;
	
	@Column(name = "STATUS")
	@Convert(converter = EBnkTrxStatusEnumConverter.class)
	private EBnkTrxStatus trxStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "TRX_DT")
	private Date trxDt;

	@Column(name = "TRX_AMT")
	private BigDecimal trxAmt;
    
    @Column(name = "CASH_POOL")
    @Convert(converter = ECashPoolEnumConverter.class)
    private ECashPool cashPool;

	@Column(name = "TRX_MEMO")
	private String trxMemo;
	
	@Column(name = "RVS_JNL_NO")
	private String rvsJnlNo;
	
	@Column(name = "RVS_FLG")
    @Convert(converter = EFlagTypeEnumConverter.class)
	private EFlagType rvsFlg;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "RVS_DT")
	private Date rvsDt;
	
	@Column(name = "RVS_APPL_NO")
	private String rvsApplNo;
	
	@Column(name = "DEAL_MEMO")
	private String dealMemo;

	@Column(name = "REL_BNK_ID")
	private String relBnkId;

	@Column(name = "CREATE_OPID")
	private String createOpid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;
	
	@Column(name = "LAST_MNT_OPID")
	private String lastMntOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MNT_TS")
	private Date lastMntTs;
	
	@Version
    @Column(name = "VERSION_CT")
	private Long versionCt;

	public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
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

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
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

	public EBnkTrxStatus getTrxStatus() {
		return trxStatus;
	}

	public void setTrxStatus(EBnkTrxStatus trxStatus) {
		this.trxStatus = trxStatus;
	}

	public Date getRvsDt() {
		return rvsDt;
	}

	public void setRvsDt(Date rvsDt) {
		this.rvsDt = rvsDt;
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

	public ERechargeWithdrawFlag getRechargeWithdrawFlag() {
		return rechargeWithdrawFlag;
	}

	public void setRechargeWithdrawFlag(ERechargeWithdrawFlag rechargeWithdrawFlag) {
		this.rechargeWithdrawFlag = rechargeWithdrawFlag;
	}
	
	public EFlagType getSignedFlg() {
		return signedFlg;
	}

	public void setSignedFlg(EFlagType signedFlg) {
		this.signedFlg = signedFlg;
	}

	public ECashPool getCashPool() {
		return cashPool;
	}

	public void setCashPool(ECashPool cashPool) {
		this.cashPool = cashPool;
	}

	public EBankType getBankCode() {
		return bankCode;
	}

	public void setBankCode(EBankType bankCode) {
		this.bankCode = bankCode;
	}

	public String getDealMemo() {
		return dealMemo;
	}

	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}

	public String getRvsApplNo() {
		return rvsApplNo;
	}

	public void setRvsApplNo(String rvsApplNo) {
		this.rvsApplNo = rvsApplNo;
	}

	public String getRvsJnlNo() {
		return rvsJnlNo;
	}

	public void setRvsJnlNo(String rvsJnlNo) {
		this.rvsJnlNo = rvsJnlNo;
	}

	public EFlagType getRvsFlg() {
		return rvsFlg;
	}

	public void setRvsFlg(EFlagType rvsFlg) {
		this.rvsFlg = rvsFlg;
	}
	
	

}
