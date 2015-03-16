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
import com.hengxin.platform.fund.entity.converter.FundApplStatusEnumConverter;
import com.hengxin.platform.fund.entity.converter.FundUseTypeEnumConverter;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundUseType;

@Entity
@Table(name="AC_TSFR_APPL")
@SuppressWarnings("serial")
@EntityListeners(IdInjectionEntityListener.class)
public class TransferApplPo implements Serializable {

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
	
	@Column(name="DEAL_MEMO")
	private String dealMemo;
	
	@Column(name="EVENT_ID")
	private String eventId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="APPL_DT")
	private Date applDt;
	
	@Column(name="PAYER_ACCT_NO")
	private String payerAcctNo;
	
	@Column(name="PAYER_NAME")
	private String payerName;
	
	@Column(name="PAYEE_ACCT_NO")
	private String payeeAcctNo;
	
	@Column(name="PAYEE_NAME")
	private String payeeName;
	
	@Column(name="USE_TYPE")
	@Convert(converter = FundUseTypeEnumConverter.class)
	private EFundUseType useType;
	
	@Column(name="TRX_AMT")
	private BigDecimal trxAmt;
	
	@Column(name="TRX_MEMO")
	private String trxMemo;
	
	@Column(name="IMP_FILE_NAME")
	private String importFileName;
	
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

	public String getDealMemo() {
		return dealMemo;
	}

	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Date getApplDt() {
		return applDt;
	}

	public void setApplDt(Date applDt) {
		this.applDt = applDt;
	}

	public String getPayerAcctNo() {
		return payerAcctNo;
	}

	public void setPayerAcctNo(String payerAcctNo) {
		this.payerAcctNo = payerAcctNo;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayeeAcctNo() {
		return payeeAcctNo;
	}

	public void setPayeeAcctNo(String payeeAcctNo) {
		this.payeeAcctNo = payeeAcctNo;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public EFundUseType getUseType() {
		return useType;
	}

	public void setUseType(EFundUseType useType) {
		this.useType = useType;
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

	public String getImportFileName() {
		return importFileName;
	}

	public void setImportFileName(String importFileName) {
		this.importFileName = importFileName;
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
	
	
}
