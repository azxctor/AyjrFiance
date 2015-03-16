package com.hengxin.platform.fund.entity;

import java.io.Serializable;
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
import com.hengxin.platform.fund.enums.EFundApplStatus;

@SuppressWarnings("serial")
@Entity
@Table(name="AC_RVS_APPL")
@EntityListeners(IdInjectionEntityListener.class)
public class ReverseApplPo implements Serializable{
	
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
	
	@Column(name="RVS_JNL_NO")
	private String rvsJnlNo;
	
	@Column(name="RVS_FRZ_JNL_NO")
	private String rvsFrzJnlNo;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="APPL_DT")
	private Date applDt;
	
	@Column(name="DEAL_MEMO")
	private String dealMemo;
	
	@Temporal(TemporalType.DATE)
	@Column(name="TRX_DT")
	private Date trxDt;
	
	@Column(name="TRX_MEMO")
	private String trxMemo;
	
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

	public String getRvsJnlNo() {
		return rvsJnlNo;
	}

	public void setRvsJnlNo(String rvsJnlNo) {
		this.rvsJnlNo = rvsJnlNo;
	}

	public Date getApplDt() {
		return applDt;
	}

	public void setApplDt(Date applDt) {
		this.applDt = applDt;
	}

	public String getDealMemo() {
		return dealMemo;
	}

	public void setDealMemo(String dealMemo) {
		this.dealMemo = dealMemo;
	}

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public String getTrxMemo() {
		return trxMemo;
	}

	public void setTrxMemo(String trxMemo) {
		this.trxMemo = trxMemo;
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

	public String getRvsFrzJnlNo() {
		return rvsFrzJnlNo;
	}

	public void setRvsFrzJnlNo(String rvsFrzJnlNo) {
		this.rvsFrzJnlNo = rvsFrzJnlNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
