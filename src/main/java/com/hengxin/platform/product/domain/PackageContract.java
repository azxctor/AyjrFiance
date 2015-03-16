package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * 融资包合同
 * @author dcliu
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PKG_CNTRCT")
public class PackageContract implements Serializable {
	
	@Id
	@Column(name = "PKG_ID")
	private String pkgId;
	
	@Column(name = "FNCR_PREPAY_PENALTY_RT")
	private BigDecimal fncrPrepayPenaltyRt;
	
	@Column(name = "PLTFRM_PREPAY_PENALTY_RT")
	private BigDecimal platformPrepayPenaltyRt;
	
	@Column(name = "PAY_PENALTY_FINE_RT")
	private BigDecimal reppayPenaltyFineRt;
	
	@Column(name = "PREPAY_DEDUCT_INTR_FLG")
	private String prePayDeductIntrFlag;

    @Column(name = "CREATE_OPID")
	private String createOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS")
	private Date createTs;
    
    @Version
    @Column(name = "VERSION_CT")
	private Long versionCt;

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public BigDecimal getFncrPrepayPenaltyRt() {
		return fncrPrepayPenaltyRt;
	}

	public void setFncrPrepayPenaltyRt(BigDecimal fncrPrepayPenaltyRt) {
		this.fncrPrepayPenaltyRt = fncrPrepayPenaltyRt;
	}

	public BigDecimal getPlatformPrepayPenaltyRt() {
		return platformPrepayPenaltyRt;
	}

	public void setPlatformPrepayPenaltyRt(BigDecimal platformPrepayPenaltyRt) {
		this.platformPrepayPenaltyRt = platformPrepayPenaltyRt;
	}

	public BigDecimal getReppayPenaltyFineRt() {
		return reppayPenaltyFineRt;
	}

	public void setReppayPenaltyFineRt(BigDecimal reppayPenaltyFineRt) {
		this.reppayPenaltyFineRt = reppayPenaltyFineRt;
	}

	public String getPrePayDeductIntrFlag() {
		return prePayDeductIntrFlag;
	}

	public void setPrePayDeductIntrFlag(String prePayDeductIntrFlag) {
		this.prePayDeductIntrFlag = prePayDeductIntrFlag;
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

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}
	
}
