package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Class Name: FeePayStePo
 * 
 * @author jishen
 * 会员缴费状态表
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AC_FEE_PAY_STE")
@IdClass(FeePayStatePoPk.class)
public class FeePayStatePo implements Serializable {

    @Id
	@Column(name = "USER_ID")
	private String userId;
	
    @Id
	@Column(name = "FEE_TYPE")
	private String feeType;
    
    @Column(name = "EVENT_ID")
    private String eventId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "START_DT")
	private Date startDt;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "END_DT")
	private Date endDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;
	
	@Column(name = "CREATE_OPID")
	private String createOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MNT_TS")
	private Date lastMntTs;
	
	@Column(name = "LAST_MNT_OPID")
	private String lastMntOpid;
	
	@Version
	@Column(name = "VERSION_CT")
	private Long versionCt;

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Date getStartDt() {
		return startDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public String getCreateOpid() {
		return createOpid;
	}

	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

	public String getLastMntOpid() {
		return lastMntOpid;
	}

	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	
	
	
}
